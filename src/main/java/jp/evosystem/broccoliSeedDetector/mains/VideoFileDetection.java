package jp.evosystem.broccoliSeedDetector.mains;

import java.awt.Toolkit;
import java.io.File;

import org.apache.commons.lang3.time.StopWatch;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import jp.evosystem.broccoliSeedDetector.components.ExtendedCanvasFrame;
import jp.evosystem.broccoliSeedDetector.constants.Configurations;

/**
 * 動画内の物体を検出.
 *
 * @author evosystem
 */
public class VideoFileDetection extends AbstractDetection {

	/**
	 * main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 対象の動画ファイル
		File targetVideofile = new File(Configurations.TARGET_VIDEO_FILE_PATH);

		// Webカメラから映像取得
		try (FrameGrabber frameGrabber = new FFmpegFrameGrabber(targetVideofile)) {
			frameGrabber.start();

			// 動画ファイルのフレームレートを取得
			double frameRate = frameGrabber.getFrameRate();

			// レコーダーを作成
			try (FrameRecorder recorder = FrameRecorder.createDefault("target/VideoFileDetection.mp4",
					frameGrabber.getImageWidth(),
					frameGrabber.getImageHeight())) {
				// 録画を開始
				if (Configurations.ENABLE_RECORDING) {
					recorder.start();
				}

				// コンバーターを作成
				OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

				// 画面を作成
				ExtendedCanvasFrame canvasFrame = new ExtendedCanvasFrame("タイトル",
						ExtendedCanvasFrame.getDefaultGamma() / frameGrabber.getGamma());

				// 取得した映像データ
				Mat grabbedImage;

				// 連続NGのフレーム数
				int ngCount = 0;

				// StopWatch
				StopWatch stopWatch = new StopWatch();

				// 画面が表示中の間ループ
				while (canvasFrame.isVisible() && (frameGrabber.getFrameNumber() < frameGrabber.getLengthInFrames())) {
					try {
						// 動画のフレームを取得
						grabbedImage = converter.convert(frameGrabber.grab());

						// 動画のフレームが存在する場合のみ処理を実行
						if (grabbedImage != null) {
							stopWatch.reset();
							stopWatch.start();

							// 画像処理
							boolean hasNg = !processTargetImageWrapper(grabbedImage, canvasFrame.getCurrentParameter());
							if (hasNg) {
								ngCount++;
							} else {
								ngCount = 0;
							}
							if (Configurations.BEEP_THRESHOLD_NG_COUNT < ngCount) {
								// 警告音を鳴らす
								Toolkit.getDefaultToolkit().beep();
							}

							// フレームを作成
							Frame frame = converter.convert(grabbedImage);

							// フレームを表示
							canvasFrame.showImage(frame);

							// フレームを録画
							if (Configurations.ENABLE_RECORDING) {
								recorder.record(frame);
							}

							// フレームレートに応じて適切にウエイトを行う
							stopWatch.stop();
							long sleepTime = (int) (1000 / frameRate) - stopWatch.getTime();
							if (0 < sleepTime) {
								Thread.sleep(sleepTime);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 画面を閉じる
				canvasFrame.dispose();
			}
		}
	}
}