package jp.evosystem.broccoliSeedDetector.mains;

import java.util.Arrays;
import java.util.Random;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

import jp.evosystem.broccoliSeedDetector.constants.Configurations;
import jp.evosystem.broccoliSeedDetector.models.ProcessImageParameter;
import jp.evosystem.broccoliSeedDetector.utils.MathHelper;

/**
 * 画像内の物体を検出.
 *
 * @author evosystem
 */
public abstract class AbstractDetection {

	/**
	 * 画像処理.
	 *
	 * @param targetImageMat
	 * @param processImageParameter
	 */
	protected static void processTargetImage(Mat targetImageMat, ProcessImageParameter processImageParameter) {
		// グレースケール画像を作成
		Mat targetImageMatGray = new Mat();
		opencv_imgproc.cvtColor(targetImageMat, targetImageMatGray, opencv_imgproc.COLOR_BGR2GRAY);

		// ヒストグラム平坦化
		Mat targetImageMatEqualizeHist = new Mat();
		opencv_imgproc.equalizeHist(targetImageMatGray,targetImageMatEqualizeHist);

		// ブラー画像を作成
		Mat targetImageMatBlur = new Mat();
		opencv_imgproc.GaussianBlur(targetImageMatGray, targetImageMatBlur,
				new Size(processImageParameter.gaussianBlueSize, processImageParameter.gaussianBlueSize), 0);

		// エッジ抽出
		Mat targetImageMatCanny = new Mat();
		opencv_imgproc.Canny(targetImageMatBlur, targetImageMatCanny, processImageParameter.cannyThreshold1,
				processImageParameter.cannyThreshold2);

		// 膨張処理
		Mat targetImageMatDilate = new Mat();
		opencv_imgproc.dilate(targetImageMatCanny, targetImageMatDilate, new Mat());

		// 収縮処理
		Mat targetImageMatErode = new Mat();
		opencv_imgproc.erode(targetImageMatDilate, targetImageMatErode, new Mat());

		// debug
		if (Configurations.ENABLE_DEBUG_MODE) {
			opencv_core.copyTo(targetImageMatErode, targetImageMat, new Mat());
		}

		// 輪郭を検出
		MatVector targetImageContours = new MatVector();
		Mat targetImageHierarchy = new Mat();
		opencv_imgproc.findContours(targetImageMatErode, targetImageContours, targetImageHierarchy,
				opencv_imgproc.RETR_EXTERNAL,
				opencv_imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("検出した輪郭数:" + targetImageContours.size());

		// 全ての輪郭を描画
		if (Configurations.DRAW_ALL_CONTOURS) {
			opencv_imgproc.drawContours(targetImageMat, targetImageContours, -1, Scalar.GREEN, 3, 0, new Mat(), 1,
					new Point(0, 0));
		}

		// 全ての輪郭に対して実行
		for (Mat contour : targetImageContours.get()) {
			// 回転を考慮しない外接矩形を取得
			Rect boundingRect = opencv_imgproc.boundingRect(contour);

			// 外接矩形の4点の座標を取得(Mat型)
			Point tl = boundingRect.tl();
			Point tr = new Point(boundingRect.x() + boundingRect.width(), boundingRect.y());
			Point br = boundingRect.br();
			Point bl = new Point(boundingRect.x(), boundingRect.y() + boundingRect.height());

			// 外接矩形の面積を計算
			double boundingReactArea = boundingRect.width() * boundingRect.height();
			if (processImageParameter.contourAreaMinThreshold < boundingReactArea
					&& boundingReactArea < processImageParameter.contourAreaMaxThreshold) {

				// 外接矩形を描画
				opencv_imgproc.rectangle(targetImageMat, boundingRect, Scalar.RED, 2, opencv_imgproc.FILLED, 0);

				// 4点の座標に円を描画
				for (Point point : Arrays.asList(tl, tr, br, bl)) {
					opencv_imgproc.circle(targetImageMat, point, 5, Scalar.RED, -1, opencv_imgproc.FILLED, 0);
				}

				// 中間点を計算
				Point tltr = MathHelper.midPoint(tl, tr);
				Point blbr = MathHelper.midPoint(bl, br);
				Point tlbl = MathHelper.midPoint(tl, bl);
				Point trbr = MathHelper.midPoint(tr, br);

				// 中間点に円を描画
				for (Point point : Arrays.asList(tltr, blbr, tlbl, trbr)) {
					opencv_imgproc.circle(targetImageMat, point, 3, Scalar.RED, -1, opencv_imgproc.FILLED, 0);
				}

				// 中間点同士を結ぶ直線を描画
				opencv_imgproc.line(targetImageMat, tltr, blbr, Scalar.GREEN);
				opencv_imgproc.line(targetImageMat, tlbl, trbr, Scalar.GREEN);

				// 判定結果を描画
				if (checkSeedExists(targetImageMat, boundingRect)) {
					opencv_imgproc.rectangle(targetImageMat, tl, new Point(tl.x() + 20, tl.y() + 20), Scalar.GREEN, -1,
							opencv_imgproc.LINE_4, 0);
					opencv_imgproc.putText(targetImageMat, "OK", new Point(tl.x(), tl.y() + 15),
							opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
							Scalar.BLACK);
				} else {
					opencv_imgproc.rectangle(targetImageMat, tl, new Point(tl.x() + 20, tl.y() + 20), Scalar.RED, -1,
							opencv_imgproc.LINE_4, 0);
					opencv_imgproc.putText(targetImageMat, "NG", new Point(tl.x(), tl.y() + 15),
							opencv_imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
							Scalar.BLACK);
				}
			}
		}
	}

	/**
	 * 矩形内に種が存在するかをチェック.
	 *
	 * @param targetImageMat
	 * @param boundingRect
	 * @return
	 */
	private static boolean checkSeedExists(Mat targetImageMat, Rect boundingRect) {
		// TODO
		return 1 < new Random().nextInt(20);
	}
}