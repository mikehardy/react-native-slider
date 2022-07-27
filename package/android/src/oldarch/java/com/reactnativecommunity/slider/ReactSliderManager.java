/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.reactnativecommunity.slider;

import android.view.View;
import android.widget.SeekBar;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Manages instances of {@code ReactSlider}.
 */
public class ReactSliderManager extends SimpleViewManager<ReactSlider> {

  static class ReactSliderShadowNode extends LayoutShadowNode implements
          YogaMeasureFunction {

    private int mWidth;
    private int mHeight;
    private boolean mMeasured;

    private ReactSliderShadowNode() {
      initMeasureFunction();
    }

    private void initMeasureFunction() {
      setMeasureFunction(this);
    }

    @Override
    public long measure(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
      if (!mMeasured) {
        SeekBar reactSlider = new ReactSlider(getThemedContext(), null);
        final int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        reactSlider.measure(spec, spec);
        mWidth = reactSlider.getMeasuredWidth();
        mHeight = reactSlider.getMeasuredHeight();
        mMeasured = true;
      }

      return YogaMeasureOutput.make(mWidth, mHeight);
    }
  }

  private static final SeekBar.OnSeekBarChangeListener ON_CHANGE_LISTENER =
          new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
              ReactContext reactContext = (ReactContext) seekbar.getContext();
              reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
                      new ReactSliderEvent(
                              seekbar.getId(),
                              ((ReactSlider)seekbar).toRealProgress(progress), fromUser));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {
              ReactContext reactContext = (ReactContext) seekbar.getContext();
              ((ReactSlider)seekbar).isSliding(true);
              reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
                      new ReactSlidingStartEvent(
                              seekbar.getId(),
                              ((ReactSlider)seekbar).toRealProgress(seekbar.getProgress())));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {
              ReactContext reactContext = (ReactContext) seekbar.getContext();
              ((ReactSlider)seekbar).isSliding(false);
              reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
                      new ReactSlidingCompleteEvent(
                              seekbar.getId(),
                              ((ReactSlider)seekbar).toRealProgress(seekbar.getProgress())));
              reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
                      new ReactSliderEvent(
                              seekbar.getId(),
                              ((ReactSlider)seekbar).toRealProgress(seekbar.getProgress()),
                              !((ReactSlider)seekbar).isSliding()));
            }
          };

  @Override
  public String getName() {
    return ReactSliderManagerImpl.REACT_CLASS;
  }

  @Override
  public LayoutShadowNode createShadowNodeInstance() {
    return new ReactSliderShadowNode();
  }

  @Override
  public Class getShadowNodeClass() {
    return ReactSliderShadowNode.class;
  }

  @Override
  protected ReactSlider createViewInstance(ThemedReactContext context) {
    return ReactSliderManagerImpl.createViewInstance(context);
  }

  @ReactProp(name = ViewProps.ENABLED, defaultBoolean = true)
  public void setEnabled(ReactSlider view, boolean enabled) {
    view.setEnabled(enabled);
  }

  @ReactProp(name = "value", defaultFloat = 0f)
  public void setValue(ReactSlider view, float value) {
    ReactSliderManagerImpl.setValue(view, value);
  }

  @ReactProp(name = "minimumValue", defaultFloat = 0f)
  public void setMinimumValue(ReactSlider view, float value) {
    ReactSliderManagerImpl.setMinimumValue(view, value);
  }

  @ReactProp(name = "maximumValue", defaultFloat = 1f)
  public void setMaximumValue(ReactSlider view, float value) {
    ReactSliderManagerImpl.setMaximumValue(view, value);
  }

  @ReactProp(name = "step", defaultFloat = 0f)
  public void setStep(ReactSlider view, float value) {
    ReactSliderManagerImpl.setStep(view, value);
  }

  @ReactProp(name = "thumbTintColor", customType = "Color")
  public void setThumbTintColor(ReactSlider view, Integer color) {
    ReactSliderManagerImpl.setThumbTintColor(view, color);
  }

  @ReactProp(name = "minimumTrackTintColor", customType = "Color")
  public void setMinimumTrackTintColor(ReactSlider view, Integer color) {
    ReactSliderManagerImpl.setMinimumTrackTintColor(view, color);
  }

  @ReactProp(name = "thumbImage")
  public void setThumbImage(ReactSlider view, @Nullable ReadableMap source) {
    ReactSliderManagerImpl.setThumbImage(view, source);
  }

  @ReactProp(name = "maximumTrackTintColor", customType = "Color")
  public void setMaximumTrackTintColor(ReactSlider view, Integer color) {
    ReactSliderManagerImpl.setMaximumTrackTintColor(view, color);
  }

  @ReactProp(name = "inverted", defaultBoolean = false)
  public void setInverted(ReactSlider view, boolean inverted) {
    ReactSliderManagerImpl.setInverted(view, inverted);
  }

  @ReactProp(name = "accessibilityUnits")
  public void setAccessibilityUnits(ReactSlider view, String accessibilityUnits) {
    ReactSliderManagerImpl.setAccessibilityUnits(view, accessibilityUnits);
  }

  @ReactProp(name = "accessibilityIncrements")
  public void setAccessibilityIncrements(ReactSlider view, ReadableArray accessibilityIncrements) {
    ReactSliderManagerImpl.setAccessibilityIncrements(view, accessibilityIncrements);
  }

  @Override
  protected void addEventEmitters(final ThemedReactContext reactContext, final ReactSlider view) {
    view.setOnSeekBarChangeListener(ON_CHANGE_LISTENER);
  }

  @Override
  public Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(ReactSlidingCompleteEvent.EVENT_NAME, MapBuilder.of("registrationName", "onRNCSliderSlidingComplete"),
            ReactSlidingStartEvent.EVENT_NAME, MapBuilder.of("registrationName", "onRNCSliderSlidingStart"));
  }
}