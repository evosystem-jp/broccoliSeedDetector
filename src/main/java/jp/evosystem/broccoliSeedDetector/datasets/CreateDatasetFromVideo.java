package jp.evosystem.broccoliSeedDetector.datasets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_imgproc.CLAHE;

import jp.evosystem.broccoliSeedDetector.components.ExtendedCanvasFrame;
import jp.evosystem.broccoliSeedDetector.constants.Configurations;
import jp.evosystem.broccoliSeedDetector.models.ProcessImageParameter;

/**
 * 動画内の物体を検出してデータセットとして保存.
 *
 * @author evosystem
 */
public class CreateDatasetFromVideo {

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
			System.out.println("frameRate : " + frameRate);

			// コンバーターを作成
			OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

			// 画面を作成
			ExtendedCanvasFrame canvasFrame = new ExtendedCanvasFrame("タイトル",
					ExtendedCanvasFrame.getDefaultGamma() / frameGrabber.getGamma());

			// 取得した映像データ
			Mat grabbedImage;

			// 画面が表示中の間ループ
			while (canvasFrame.isVisible() && (frameGrabber.getFrameNumber() < frameGrabber.getLengthInFrames())) {
				try {
					// 動画のフレームを取得
					grabbedImage = converter.convert(frameGrabber.grab());

					// 動画のフレームが存在する場合のみ処理を実行
					if (grabbedImage != null) {

						// 4.5秒分のフレームにつき1回処理を実行
						if (frameGrabber.getFrameNumber() % (int) (frameRate * 4.5) == 0) {
							System.out.println("frameNumber : " + frameGrabber.getFrameNumber());

							// 画像処理
							processTargetImage(grabbedImage, canvasFrame.getCurrentParameter());
							// フレームを作成
							Frame frame = converter.convert(grabbedImage);

							// フレームを表示
							canvasFrame.showImage(frame);
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

	/**
	 * 画像処理.
	 *
	 * @param targetImageMat
	 * @param processImageParameter
	 * @throws IOException
	 */
	protected static void processTargetImage(Mat targetImageMat, ProcessImageParameter processImageParameter)
			throws IOException {
		// 元画像のコピーを作成
		Mat targetImageMatClone = targetImageMat.clone();

		// グレースケール画像を作成
		Mat targetImageMatGray = new Mat();
		opencv_imgproc.cvtColor(targetImageMat, targetImageMatGray, opencv_imgproc.COLOR_BGR2GRAY);

		// ヒストグラム平坦化
		CLAHE clahe = opencv_imgproc.createCLAHE(processImageParameter.claheClipLimit,
				new Size(processImageParameter.claheTileGridSize, processImageParameter.claheTileGridSize));
		Mat targetImageMatEqualizeHist = new Mat();
		clahe.apply(targetImageMatGray, targetImageMatEqualizeHist);

		// ガンマ補正
		double gamma = 2.5;
		Mat lut = new Mat(1, 256, opencv_core.CV_8U);
		UByteIndexer lutIndexer = lut.createIndexer();
		for (int i = 0; i < 256; i++) {
			double lutValue = Math.pow((i / 255.0), (1.0 / gamma)) * 255;
			lutIndexer.put(i, (int) lutValue);
		}
		Mat targetImageMatLut = new Mat();
		opencv_core.LUT(targetImageMatEqualizeHist, lut, targetImageMatLut);

		// ブラー画像を作成
		Mat targetImageMatBlur = new Mat();
		opencv_imgproc.GaussianBlur(targetImageMatLut, targetImageMatBlur,
				new Size(processImageParameter.gaussianBlueSize, processImageParameter.gaussianBlueSize), 0);

		// エッジ抽出
		Mat targetImageMatCanny = new Mat();
		opencv_imgproc.Canny(targetImageMatBlur, targetImageMatCanny, processImageParameter.cannyThreshold1,
				processImageParameter.cannyThreshold2);

		// 膨張処理
		Mat targetImageMatDilate = new Mat();
		opencv_imgproc.dilate(targetImageMatCanny, targetImageMatDilate, new Mat(), new Point(-1, -1),
				processImageParameter.dilateIterations, opencv_core.BORDER_CONSTANT,
				opencv_imgproc.morphologyDefaultBorderValue());

		// 収縮処理
		Mat targetImageMatErode = new Mat();
		opencv_imgproc.erode(targetImageMatDilate, targetImageMatErode, new Mat(), new Point(-1, -1),
				processImageParameter.erodeIterations, opencv_core.BORDER_CONSTANT,
				opencv_imgproc.morphologyDefaultBorderValue());

		// 輪郭を検出
		MatVector targetImageContours = new MatVector();
		Mat targetImageHierarchy = new Mat();
		opencv_imgproc.findContours(targetImageMatErode, targetImageContours, targetImageHierarchy,
				opencv_imgproc.RETR_EXTERNAL,
				opencv_imgproc.CHAIN_APPROX_SIMPLE);

		// 全ての輪郭を描画
		if (Configurations.DRAW_ALL_CONTOURS) {
			opencv_imgproc.drawContours(targetImageMat, targetImageContours, -1, Scalar.GREEN, 3, 0, new Mat(), 1,
					new Point(0, 0));
		}

		// 全ての輪郭に対して実行
		for (Mat contour : targetImageContours.get()) {
			// 回転を考慮しない外接矩形を取得
			Rect boundingRect = opencv_imgproc.boundingRect(contour);

			// 外接矩形の面積を計算
			double boundingReactArea = boundingRect.width() * boundingRect.height();
			if (processImageParameter.contourAreaMinThreshold < boundingReactArea
					&& boundingReactArea < processImageParameter.contourAreaMaxThreshold) {
				// 外接矩形を描画
				opencv_imgproc.rectangle(targetImageMat, boundingRect, Scalar.RED, 2, opencv_imgproc.FILLED, 0);

				// 矩形を保存
				saveSubTargetImage(targetImageMatClone, boundingRect);
			}
		}
	}

	/**
	 * 矩形を保存.
	 *
	 * @param targetImageMat
	 * @param boundingRect
	 * @return
	 * @throws IOException
	 */
	private static void saveSubTargetImage(Mat targetImageMat, Rect boundingRect) throws IOException {
		// 矩形内の画像を抽出
		Mat subTargetImageMat = targetImageMat.apply(boundingRect);

		// 保存先ファイルを作成
		File file = new File("datasets/raw", String.format("%d.jpg", System.currentTimeMillis()));
		Files.createDirectories(file.getParentFile().toPath());

		// 画像を保存
		opencv_imgcodecs.imwrite(file.getAbsolutePath(), subTargetImageMat);
	}
}