package jp.evosystem.broccoliSeedDetector.mains;

import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.IPCameraFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import jp.evosystem.broccoliSeedDetector.components.ExtendedCanvsFrame;
import jp.evosystem.broccoliSeedDetector.constants.Configurations;

/**
 * IPカメラ画像から画像内の物体を検出.
 *
 * @author evosystem
 */
public class IpCameraDetection extends AbstractDetection {

	/**
	 * main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// IPカメラから映像取得
		try (FrameGrabber frameGrabber = new IPCameraFrameGrabber(Configurations.TARGET_IP_CAMERA_URL, 1, 1,
				TimeUnit.MINUTES)) {
			frameGrabber.start();

			// レコーダーを作成
			try (FrameRecorder recorder = FrameRecorder.createDefault("target/WebCameraDetection.avi",
					frameGrabber.getImageWidth(),
					frameGrabber.getImageHeight())) {
				// 録画を開始
				if (Configurations.ENABLE_RECORDING) {
					recorder.start();
				}

				// コンバーターを作成
				OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

				// 画面を作成
				ExtendedCanvsFrame canvasFrame = new ExtendedCanvsFrame("タイトル",
						ExtendedCanvsFrame.getDefaultGamma() / frameGrabber.getGamma());

				// 取得した映像データ
				Mat grabbedImage;

				// 連続NGのフレーム数
				int ngCount = 0;

				// 画面が表示中の間ループ
				while (canvasFrame.isVisible() && (grabbedImage = converter.convert(frameGrabber.grab())) != null) {
					try {
						// 画像処理
						boolean hasNg = processTargetImage(grabbedImage, canvasFrame.getCurrentParameter());
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