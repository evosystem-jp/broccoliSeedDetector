package jp.evosystem.broccoliSeedDetector.models;

import jp.evosystem.broccoliSeedDetector.constants.Configurations;

/**
 * 画像処理用のパラメータ.
 *
 * @author evosystem
 */
public class ProcessImageParameter {

	/**
	 * ブラーのサイズ(奇数).
	 */
	public int gaussianBlueSize;

	/**
	 * Canny処理のしきい値1.
	 */
	public int cannyThreshold1;

	/**
	 * Canny処理のしきい値2.
	 */
	public int cannyThreshold2;

	/**
	 * 輪郭の面積の最小値.
	 */
	public int contourAreaMinThreshold;

	/**
	 * 輪郭の面積の最小値.
	 */
	public int contourAreaMaxThreshold;

	/**
	 * デフォルトのパラメータを取得.
	 *
	 * @return
	 */
	public static ProcessImageParameter getDefaultParameter() {
		ProcessImageParameter parameter = new ProcessImageParameter();
		parameter.gaussianBlueSize = Configurations.DEFAULT_GAUSSIAN_BLUR_SIZE;
		if (parameter.gaussianBlueSize % 2 == 0) {
			parameter.gaussianBlueSize++;
		}
		parameter.cannyThreshold1 = Configurations.DEFAULT_CANNY_THRESHOLD_1;
		parameter.cannyThreshold2 = Configurations.DEFAULT_CANNY_THRESHOLD_2;
		parameter.contourAreaMinThreshold = Configurations.DEFAULT_CONTOUR_AREA_MIN_THRESHOLD;
		parameter.contourAreaMaxThreshold = Configurations.DEFAULT_CONTOUR_AREA_MAX_THRESHOLD;
		return parameter;
	}
}