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
	 * CLAHEのclipLimit.
	 */
	public double claheClipLimit;

	/**
	 * CLAHEのタイルサイズ.
	 */
	public int claheTileGridSize;

	/**
	 * エッジ抽出処理のしきい値1.
	 */
	public int cannyThreshold1;

	/**
	 * エッジ抽出処理のしきい値2.
	 */
	public int cannyThreshold2;

	/**
	 * 膨張処理の繰り返し回数.
	 */
	public int dilateIterations;

	/**
	 * 収縮処理の繰り返し回数.
	 */
	public int erodeIterations;

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
		parameter.claheClipLimit = Configurations.DEFAULT_CLAHE_CLIP_LIMIT;
		parameter.claheTileGridSize = Configurations.DEFAULT_CLAHE_TILE_GRID_SIZE;
		parameter.cannyThreshold1 = Configurations.DEFAULT_CANNY_THRESHOLD_1;
		parameter.cannyThreshold2 = Configurations.DEFAULT_CANNY_THRESHOLD_2;
		parameter.dilateIterations = Configurations.DEFAULT_DILATE_ITERATIONS;
		parameter.erodeIterations = Configurations.DEFAULT_ERODE_ITERATIONS;
		parameter.contourAreaMinThreshold = Configurations.DEFAULT_CONTOUR_AREA_MIN_THRESHOLD;
		parameter.contourAreaMaxThreshold = Configurations.DEFAULT_CONTOUR_AREA_MAX_THRESHOLD;
		return parameter;
	}
}