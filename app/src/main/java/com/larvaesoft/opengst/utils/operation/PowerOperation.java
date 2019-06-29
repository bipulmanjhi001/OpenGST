package com.larvaesoft.opengst.utils.operation;

import com.larvaesoft.opengst.utils.operation.base.BinaryOperation;
import com.larvaesoft.opengst.utils.operation.base.Operation;

public class PowerOperation extends BinaryOperation implements Operation {

    protected PowerOperation(double baseValue, double secondValue) {
        super(baseValue, secondValue);
    }

    @Override
    public double getResult() {
        double result = Math.pow(baseValue, secondValue);
        if (Double.isInfinite(result) || Double.isNaN(result))
            result = 0;
        return result;
    }
}
