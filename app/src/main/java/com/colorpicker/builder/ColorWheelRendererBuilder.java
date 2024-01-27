package com.colorpicker.builder;

import com.colorpicker.ColorPickerView;
import com.colorpicker.renderer.ColorWheelRenderer;
import com.colorpicker.renderer.FlowerColorWheelRenderer;
import com.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
	public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
		switch (wheelType) {
			case CIRCLE:
				return new SimpleColorWheelRenderer();
			case FLOWER:
				return new FlowerColorWheelRenderer();
		}
		throw new IllegalArgumentException("wrong WHEEL_TYPE");
	}
}