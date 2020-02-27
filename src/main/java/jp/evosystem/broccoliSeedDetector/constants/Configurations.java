package jp.evosystem.broccoliSeedDetector.constants;

/**
 * 環境設定.
 *
 * @author evosystem
 */
public interface Configurations {

	/**
	 * デバッグモードを有効化するかどうか.
	 */
	boolean ENABLE_DEBUG_MODE = false;

	/**
	 * ブラーのサイズのデフォルト値(奇数).
	 */
	int DEFAULT_GAUSSIAN_BLUR_SIZE = 20;

	/**
	 * Canny処理のしきい値1のデフォルト値.
	 */
	int DEFAULT_CANNY_THRESHOLD_1 = 25;

	/**
	 * Canny処理のしきい値2のデフォルト値.
	 */
	int DEFAULT_CANNY_THRESHOLD_2 = 50;

	/**
	 * 輪郭の面積の最小値のデフォルト値.
	 */
	int DEFAULT_CONTOUR_AREA_MIN_THRESHOLD = 20000;

	/**
	 * 輪郭の面積の最大値のデフォルト値.
	 */
	int DEFAULT_CONTOUR_AREA_MAX_THRESHOLD = 30000;

	/**
	 * 全ての輪郭を描画するかどうか.
	 */
	boolean DRAW_ALL_CONTOURS = false;

	/**
	 * 処理結果をファイルに出力するかどうか.
	 */
	boolean ENABLE_RECORDING = false;

	/**
	 * 対象の画像ファイルのパス.
	 */
	String TARGET_IMAGE_FILE_PATH = "images/example.jpg";

	/**
	 * 対象の動画ファイルのパス.
	 */
	String TARGET_VIDEO_FILE_PATH = "videos/example.mov";

	/**
	 * 使用するWebカメラのデバイス番号.
	 */
	int TARGET_DEVICE_NUMBER = 0;
}