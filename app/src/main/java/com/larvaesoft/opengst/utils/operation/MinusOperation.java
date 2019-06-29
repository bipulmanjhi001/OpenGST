package com.larvaesoft.opengst.utils.operation;

import com.larvaesoft.opengst.utils.operation.base.BinaryOperation;
import com.larvaesoft.opengst.utils.operation.base.Operation;

public class MinusOperation extends BinaryOperation implements Operation {
    protected MinusOperation(double baseValue, double secondValue) {
        super(baseValue, secondValue);
    }

    @Override
    public double getResult() {
        return baseValue - secondValue;
    }
}
