package jp.evosystem.broccoliSeedDetector.mains;

import java.awt.Toolkit;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import jp.evosystem.broccoliSeedDetector.components.ExtendedCanvasFrame;
import jp.evosystem.broccoliSeedDetector.constants.Configurations;

/**
 * Webカメラ画像から画像内の物体を検出.
 *
 * @author evosystem
 */
public class WebCameraDetection extends AbstractDetection {

	/**
	 * main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Webカメラから映像取得
		try (FrameGrabber frameGrabber = FrameGrabber.createDefault(Configurations.TARGET_DEVICE_NUMBER)) {
			frameGrabber.start();

			// レコーダーを作成
			try (FrameRecorder recorder = FrameRecorder.createDefault("target/WebCameraDetection.mp4",
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

				// 画面が表示中の間ループ
				while (canvasFrame.isVisible() && (grabbedImage = converter.convert(frameGrabber.grab())) != null) {
					try {
						// 画像処理
						boolean hasNg = processTargetImageWrapper(grabbedImage, canvasFrame.getCurrentParameter());
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