package com.company;

import java.math.BigInteger;

public class Solution {
    public static String solution(int[] xs) {
        BigInteger finalResult = new BigInteger("0");
        SolarPanelsDescriptor panel = new SolarPanelsDescriptor(xs);
        panel.analyseAndSplitSolarPanelData();
        String state = panel.getSolarPanelState();

        switch(state) {
            case "ALL_DRAINING":
            case "NOT_WORKING_AND_DRAINING":
                finalResult = panel.getDrainingPanelsConsumption();
                break;
            case "WORKING_AND_DRAINING":
            case "WORKING_AND_DRAINING_AND_NOT_WORKING":
                finalResult = panel.getDrainingPanelsConsumption().multiply(panel.getWorkingPanelsConsumption());
                break;
            case "ALL_NOT_WORKING":
            case "NOT_WORKING_AND_1DRAINING":
                break;  // default zero
            case "ONE_DRAINING":
                finalResult = toBigInt(panel.getMinimalPowerDrainage());
                break;
            default:
                finalResult = panel.getWorkingPanelsConsumption();
        }
        return finalResult.toString();
    }

    //  state = working(positive output) | not working(zero output) | draining(negative output) | draining1()
    //  State that go to default is:
    //    case "ALL_WORKING":
    //    case "WORKING_AND_1DRAINING":
    //    case "WORKING_AND_NOT_WORKING":
    //    case "WORKING_AND_1DRAINING_AND_NOT_WORKING":
    //    finalResult = panel.getWorkingPanelsConsumption();
    //    break;

    public static BigInteger toBigInt(int integer){
        return new BigInteger(Integer.toString(integer));
    }
}

class SolarPanelsDescriptor {
    private int[] solarPanels;
    private int[] drainingPanels = new int[50];
    private BigInteger workingPanelsConsumption = new BigInteger("1");
    private BigInteger drainingPanelsConsumption = new BigInteger("1");
    private int minimalPowerDrainage = -1001;
    private int drainingQty = 0;
    private int workingQty = 0;
    private int notWorkingQty = 0;
    private BigInteger zero = new BigInteger("0");

    public SolarPanelsDescriptor(int[] solarPanels) {
        this.solarPanels = solarPanels;
    }

    public void analyseAndSplitSolarPanelData() {
        for (int panel : solarPanels) {
            if (panelIsWorking(panel)) {
                ++workingQty;
                workingPanelsConsumption = workingPanelsConsumption.multiply(toBigInt(panel));
            } else if (panelIsDraining(panel)) {
                drainingPanels[drainingQty++] = panel;
                drainingPanelsConsumption = drainingPanelsConsumption.multiply(toBigInt(panel));
                if (panel > minimalPowerDrainage) {
                    minimalPowerDrainage = panel;
                }
            } else if (panelIsNotWorking(panel)) {
                ++notWorkingQty;
            }
        }
        if (drainingPanelsConsumption.compareTo(zero) < 0){
            drainingPanelsConsumption = drainingPanelsConsumption.divide(toBigInt(minimalPowerDrainage));
        }
    }

    public BigInteger getWorkingPanelsConsumption() {
        return workingPanelsConsumption;
    }

    public BigInteger getDrainingPanelsConsumption() {
        return drainingPanelsConsumption;
    }

    public int getMinimalPowerDrainage() {
        return minimalPowerDrainage;
    }

    public boolean panelIsDraining(int panel) {
        return panel < 0;
    }

    public boolean panelIsWorking(int panel) {
        return panel > 0;
    }

    public boolean panelIsNotWorking(int panel) {
        return panel == 0;
    }

    public String getSolarPanelState() {
        Object[][] stateInspector = new Object[][]{
                {"ALL_WORKING", workingQty == solarPanels.length},
                {"ONE_DRAINING", drainingQty == solarPanels.length && drainingQty == 1},
                {"ALL_DRAINING", drainingQty == solarPanels.length && drainingQty > 1},
                {"ALL_NOT_WORKING", notWorkingQty == solarPanels.length},
                {"WORKING_AND_1DRAINING", workingQty > 0 && drainingQty == 1 && notWorkingQty == 0},
                {"WORKING_AND_DRAINING", workingQty > 0 && drainingQty > 1 && notWorkingQty == 0},
                {"WORKING_AND_NOT_WORKING", workingQty > 0 && notWorkingQty > 0 && drainingQty == 0},
                {"NOT_WORKING_AND_1DRAINING", drainingQty == 1 && notWorkingQty > 0 && workingQty == 0},
                {"NOT_WORKING_AND_DRAINING", drainingQty > 1 && notWorkingQty > 0 && workingQty == 0},
                {"WORKING_AND_1DRAINING_AND_NOT_WORKING", workingQty > 0 && drainingQty == 1 && notWorkingQty > 0},
                {"WORKING_AND_DRAINING_AND_NOT_WORKING", workingQty > 0 && drainingQty > 1 && notWorkingQty > 0},
        };
        for (Object[] row : stateInspector) {
            if ((Boolean) row[1]) {
                return (String) row[0];
            }
        }
        return "Error";
    }
    public BigInteger toBigInt(int integer){
        return new BigInteger(Integer.toString(integer));
    }
}