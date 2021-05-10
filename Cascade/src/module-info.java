module cascade {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.base;
	opens cascade to javafx.graphics;
}