import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputListener implements ActionListener {
    CalculateView cal;
    ComputationExpressions compute = new ComputationExpressions();
    public void setView (CalculateView cal) {
        this.cal = cal;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cal.leftBracket) {
            cal.inputExpression.insert("(", cal.inputExpression.getCaret().getDot());   //捕获鼠标光标位置，并插入"("
        }
        else if (e.getSource() == cal.rightBracket) {
            cal.inputExpression.insert(")", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.reciprocal) {
            cal.inputExpression.insert("^(-1)", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.abs) {
            cal.inputExpression.insert("abs()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);  //光标移到||之间
        }
        else if (e.getSource() == cal.AC) {
            cal.inputExpression.setText(null);  //清空文本框
        }
        else if (e.getSource() == cal.division) {
            cal.inputExpression.insert("÷", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.multiple) {
            cal.inputExpression.insert("×", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.DEL) {
            int index;
            index = cal.inputExpression.getCaret().getDot();
            if (index != 0) {  //防止堆栈溢出
                String temp1 = cal.inputExpression.getText().substring(0, index - 1);
                String temp2 = cal.inputExpression.getText().substring(index);
                cal.inputExpression.setText(temp1 + temp2);
                cal.inputExpression.setCaretPosition(index - 1);
                //删去光标的前一个字母
            }
        }
        else if (e.getSource() == cal.square) {
            cal.inputExpression.insert("^2", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.cube) {
            cal.inputExpression.insert("^3", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.power) {
            cal.inputExpression.insert("^", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.getInt) {
            cal.inputExpression.insert("INT()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);  //光标移到[]之间
        }
        else if (e.getSource() == cal.num[7]) {
            cal.inputExpression.insert("7", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[8]) {
            cal.inputExpression.insert("8", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[9]) {
            cal.inputExpression.insert("9", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.dash) {
            cal.inputExpression.insert("-", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.sin) {
            cal.inputExpression.insert("sin()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.cos) {
            cal.inputExpression.insert("cos()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.tan) {
            cal.inputExpression.insert("tan()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.factorial) {
            cal.inputExpression.insert("!", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[4]) {
            cal.inputExpression.insert("4", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[5]) {
            cal.inputExpression.insert("5", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[6]) {
            cal.inputExpression.insert("6", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.plus) {
            cal.inputExpression.insert("+", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.asin) {
            cal.inputExpression.insert("arcsin()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.acos) {
            cal.inputExpression.insert("arccos()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.atan) {
            cal.inputExpression.insert("arctan()", cal.inputExpression.getCaret().getDot());
            cal.inputExpression.setCaretPosition(cal.inputExpression.getCaret().getDot() - 1);
        }
        else if (e.getSource() == cal.mod) {
            cal.inputExpression.insert("mod", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[1]) {
            cal.inputExpression.insert("1", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[2]) {
            cal.inputExpression.insert("2", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[3]) {
            cal.inputExpression.insert("3", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.squareRoot) {
            cal.inputExpression.insert("^(0.5)", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.log) {
            cal.inputExpression.insert("log", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.ln) {
            cal.inputExpression.insert("ln", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.EXP) {
            cal.inputExpression.insert("E", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.pi) {
            cal.inputExpression.insert("π", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.percent) {
            cal.inputExpression.insert("%", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.num[0]) {
            cal.inputExpression.insert("0", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.dot) {
            cal.inputExpression.insert(".", cal.inputExpression.getCaret().getDot());
        }
        else if (e.getSource() == cal.equal) {
            for (int i = 0; i < cal.inputExpression.getText().length(); i++) {
                if (cal.inputExpression.getText().charAt(i) == '\n') {  //每次按下等号，都要消去上一次得到的答案
                    cal.inputExpression.setText
                            (cal.inputExpression.getText().substring(0, i));
                }
            }
            compute.setView(cal);
            compute.setExpression();
            try {
                compute.getVariables();
                cal.inputExpression.append("\n=" + compute.getResult());
            }
            catch (ComputeError ex) {
                ex.setView(cal);
                ex.showMess();
            }
        }
        if (e.getSource() instanceof JButton) {
            cal.inputExpression.getCaret().setVisible(true);    //显示光标
        }
    }
}