package uj.wmii.pwj.spreadsheet;

public class Spreadsheet {
    public String getReference( String[][] input, String cell){
        int columnCoordinate = cell.charAt(cell.indexOf('$') + 1) - 'A';
        int rowCoordinate = Integer.parseInt(cell.substring(cell.indexOf('$') + 2)) - 1;
        return input[rowCoordinate][columnCoordinate];
    }

    private Formula getFormula(String formula){
        return switch (formula) {
            case "ADD" -> Formula.ADD;
            case "SUB" -> Formula.SUB;
            case "MUL" -> Formula.MUL;
            case "DIV" -> Formula.DIV;
            default -> Formula.MOD;
        };
    }
    public String calculateCell( String[][] input, String cell){
        if(cell.charAt(0) == '$'){ // reference to number
            return calculateCell(input, getReference(input, cell));
        }else if(cell.charAt(0) == '='){ // formula
            int a = Integer.parseInt(calculateCell(input, cell.substring(cell.indexOf('(') + 1, cell.indexOf(','))));
            int b = Integer.parseInt(calculateCell(input, cell.substring(cell.indexOf(',') + 1, cell.indexOf(')'))));

            Formula formula = getFormula(cell.substring(cell.indexOf('=') + 1, cell.indexOf('(')));
            return calculateCell(input, String.valueOf(formula.count(a, b)));
        }else { // number without reference
            return cell;
        }
    }

    public String[][] calculate(String[][] input) {
        for(int row = 0; row < input.length; ++row){
            for(int column = 0; column < input[row].length; ++column){
                input[row][column] = calculateCell(input, input[row][column]);
            }
        }
        return input;
    }

    enum Formula{
        ADD("ADD"){
            public int count(int a, int b){return a + b;}
        },
        SUB("SUB"){
            public int count(int a, int b){return a - b;}
        },
        MUL("MUL"){
            public int count(int a, int b){return a * b;}
        },
        DIV("DIV"){
            public int count(int a, int b){return a / b;}
        },
        MOD("MOD"){
            public int count(int a, int b){return a % b;}
        };

        private final String formula;
        Formula(String formula){
            this.formula = formula;
        }
        abstract int count(int a, int b);
    }
}
