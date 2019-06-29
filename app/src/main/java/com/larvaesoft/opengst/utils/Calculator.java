package com.larvaesoft.opengst.utils;

import com.larvaesoft.opengst.model.GstView;

import org.jetbrains.annotations.NotNull;

public interface Calculator {
    void setValue(String value);

    void setValueDouble(double d);

    void setFormula(String value);

    void showGSTDialog(GstView g);

    void showMessage(@NotNull String s);
}
