package com.larvaesoft.opengst.utils.operation;


import com.larvaesoft.opengst.utils.operation.base.BinaryOperation;
import com.larvaesoft.opengst.utils.operation.base.Operation;

public class ModuloOperation extends BinaryOperation implements Operation {
    protected ModuloOperation(double baseValue, double secondValue) {
        super(baseValue, secondValue);
    }

    @Override
    public double getResult() {
        double result = 0;
        if (secondValue != 0) {
            result = baseValue % secondValue;
        }
        return result;
    }
}
