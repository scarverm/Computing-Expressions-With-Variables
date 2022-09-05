import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class ComputationExpressions {
    double result;
    String expression;
    CalculateView cal;
    public void setView(CalculateView cal) {
        this.cal = cal;
    }
    public void setExpression() {
        expression = cal.inputExpression.getText();
    }
    public void getVariables () throws ComputeError{   //获取表达式中的变量
        /*在获取变量时要考虑到表达式中出现的不是变量名的字母，比如可能会出现sin、cos、tan、arcsin、arccos、arctan、INT、abs、E、
        * log、ln、mod
        * 所以在使用变量名的时候应尽量使变量名不包含以上单词或字母
        */
        List<String> list = new LinkedList<>();   //利用集合去除重复的变量
        String exStr = expression;
        exStr = exStr.replaceAll("\\p{Punct}", " ");    //把所有关键字和符号替换为空格
        exStr = exStr.replaceAll("\\d", " ");   //把数字也换为空格，因此变量名应仅由字母组成
        exStr = exStr.replaceAll("log", " ");
        exStr = exStr.replaceAll("ln", " ");
        exStr = exStr.replaceAll("arcsin", " ");
        exStr = exStr.replaceAll("arccos", " ");
        exStr = exStr.replaceAll("arctan", " ");
        exStr = exStr.replaceAll("sin", " ");
        exStr = exStr.replaceAll("cos", " ");
        exStr = exStr.replaceAll("tan", " ");
        exStr = exStr.replaceAll("INT", " ");
        exStr = exStr.replaceAll("abs", " ");
        exStr = exStr.replaceAll("[Eπ×÷]", " ");
        exStr = exStr.replaceAll("mod", " ");
        String[] variables = exStr.split("[ ]+");  //以空格为分隔符，得到变量名
        for (String variable : variables) {
            if ("log ln arcsin arccos arctan sin cos tan INT abs mod".contains(variable)) {
                //如果输入的变量名是这些的子串，则抛出异常
                //用空格隔开
                throw new ComputeError("请注意设置的变量名不能为运算符号的子串");
            }
            if (!list.contains(variable)) {
                list.add(variable);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String s = JOptionPane.showInputDialog(cal, "请输入变量" + list.get(i) + "的值(" +
                    (i + 1) + "/" + list.size() + ")",
                    "输入变量", JOptionPane.PLAIN_MESSAGE);
            while (expression.contains(list.get(i))) {
                expression = replace(expression, s, expression.indexOf(list.get(i)),
                        expression.indexOf(list.get(i)) + list.get(i).length() - 1);
            }
        }
        expression = "(" + expression + ")";    //因为表达式的求值通过获取最内层括号来计算，所以为表达式最外层也加上括号
    }
    public String getResult() throws ComputeError{
        /*对表达式的计算方法：①按优先级处理各运算，先把π和e转为小数。②处理阶乘，对数和百分号。③再处理幂函数和求根。
        * ④再求三角函数，绝对值，取整。⑤求余，乘，除。⑥最后求加，减。
        */
        String temp;
        double num1, num2;  //作为运算符的运算操作数
        int dotCount = 0;   //计算一串数字中小数点的个数，如果大于1，则要抛出错误信息
        int dashCount = 0;  //减号有时候做减法，有时候做负数的符号，所以当获取一串数的时候要判断在哪个负号处停下
        int leftFixed = expression.indexOf("(");    //找第一个括号
        int leftNotFixed = expression.indexOf("(", leftFixed + 1);
        int rightFixed = expression.indexOf(")", leftFixed);
        while (leftNotFixed != -1 && rightFixed > leftFixed) {   //如果左括号不是最内层括号的左括号，则寻找下一个左括号
            leftFixed = expression.indexOf("(", leftFixed + 1);
            leftNotFixed = expression.indexOf("(", leftFixed + 1);
            rightFixed = expression.indexOf(")", leftFixed);
        }
        if (leftFixed != -1) {   //找到最里层括号
            temp = expression.substring(leftFixed + 1, rightFixed); //复制括号里的表达式
            if (temp.contains("π")) {
                int symbolPosition = temp.indexOf("π");
                if ((symbolPosition != 0 && temp.charAt(symbolPosition - 1) >= '0' &&
                        temp.charAt(symbolPosition - 1) <= '9') ||
                        (symbolPosition != 0 && temp.charAt(symbolPosition - 1) == '.')) {
                    throw new ComputeError("请确认π前的格式正确");
                }
                if ((symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) >= '0' &&
                        temp.charAt(symbolPosition + 1) <= '9') ||
                        (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) == '.')) {
                    throw new ComputeError("请确认π后的格式正确");
                }
                result = Math.PI;
                temp = replace(temp, String.valueOf(result), symbolPosition, symbolPosition);
                expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("E")) {
                int symbolPosition = temp.indexOf("E");
                if ((symbolPosition != 0 && temp.charAt(symbolPosition - 1) >= '0' &&
                        temp.charAt(symbolPosition - 1) <= '9') ||
                        (symbolPosition != 0 && temp.charAt(symbolPosition - 1) == '.')) {
                    throw new ComputeError("请确认E前的格式正确");
                }
                if ((symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) >= '0' &&
                        temp.charAt(symbolPosition + 1) <= '9') ||
                        (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) == '.')) {
                    throw new ComputeError("请确认E后的格式正确");
                }
                result = Math.E;
                temp = replace(temp, String.valueOf(result), symbolPosition, symbolPosition);
                expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("!")) {   //如果包含阶乘
                int position2 = temp.indexOf("!");  //找到第一个阶乘的位置
                int position1 = 0;  //找到数的位置
                for (int i = position2 - 1; i >= 0; i--) {   //向前找阶乘作用的数
                    if (temp.charAt(i) == '.') {
                        //如果这个数不是整数，则要抛出错误信息
                        throw new ComputeError("请确认“!”前是整数");
                    }
                    if (temp.charAt(i) == '-' && i == 0) {
                        //判断这个数是否是负数，如果是负数，则要抛出错误信息
                        throw new ComputeError("请确认“!”前是正数");
                    }
                    else if (temp.charAt(i) == '-' && i != 0 && (temp.charAt(i - 1) < '0' || temp.charAt(i - 1) > '9') ) {
                        //判断这个数是否是负数，如果是负数，则要抛出错误信息
                        throw new ComputeError("请确认“!”前是正数");
                    }
                    if (i == 0 && temp.charAt(i) > '0' && temp.charAt(i) < '9') {
                        position1 = i;
                        break;
                    }
                    else if (temp.charAt(i) < '0' || temp.charAt(i) > '9') {
                        position1 = i + 1;
                        break;
                    }
                }
                result = jieCheng(Integer.parseInt(temp.substring(position1, position2)));
                temp = replace(temp, String.valueOf((int)result), position1, position2);
                expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                //再将expression替换为修改了temp后的表达式，但在temp表达式未计算完全时，要将这个最里层括号保存下来
                if (digui(temp)) {  //判断是否递归，即判断temp中是不是只剩下了纯数
                    getResult();    //递归调用
                }
            }
            else if (temp.contains("log")) {
                int position1 = temp.indexOf("log");    //找到log的首字母位置
                int position2 = 0;
                if (position1 + 2 == temp.length() - 1) {   //如果log后面没有数字
                    throw new ComputeError("请确认log后存在数字");
                }
                if (position1 != 0 && temp.charAt(position1 - 1) >= '0' && temp.charAt(position1 - 1) <= '9') {
                    throw new ComputeError("请确认log左边的写法是否合法");
                }
                else if (temp.charAt(position1 + 3) < '0' || temp.charAt(position1 + 3) > '9'
                        || temp.charAt(position1 + 3) == '.') {
                    throw new ComputeError("请确认log后存在数字或是一个完整的数字且数字不为负");
                }
                for (int i = position1 + 3; i < temp.length(); i++) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认小数格式正确");
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position2 = i - 1;  //找到log作用数的最后一个数字的位置
                        break;
                    }
                    position2 = i;  //如果没找到其他符号，则数的位置在最后
                }
                result = Math.log10(Double.parseDouble(temp.substring(position1 + 3, position2 + 1)));
                if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                        && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                    //判断一个数能否用整型代替浮点型
                    temp = replace(temp, String.valueOf((int) result), position1, position2);
                }
                else {
                    temp = replace(temp, String.valueOf(result), position1, position2);
                }
                expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("ln")) { //对ln的求解和对log的求解类似
                int position1 = temp.indexOf("ln");
                int position2 = 0;
                if (position1 + 1 == temp.length() - 1) {
                    throw new ComputeError("请确认ln后存在数字");
                }
                else if (temp.charAt(position1 + 2) < '0' || temp.charAt(position1 + 2) > '9'
                        || temp.charAt(position1 + 2) == '.') {
                    throw new ComputeError("请确认ln后存在数字或是一个完整的数字且数字不为负");
                }
                if (position1 != 0 && temp.charAt(position1 - 1) >= '0' && temp.charAt(position1 - 1) <= '9') {
                    throw new ComputeError("请确认ln左边的写法是否合法");
                }
                for (int i = position1 + 2; i < temp.length(); i++) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认小数格式正确");
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position2 = i - 1;
                        break;
                    }
                    position2 = i;
                }
                result = Math.log(Double.parseDouble(temp.substring(position1 + 2, position2 + 1)));
                if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                        && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                    //判断一个数能否用整型代替浮点型
                    temp = replace(temp, String.valueOf((int) result), position1, position2);
                }
                else {
                    temp = replace(temp, String.valueOf(result), position1, position2);
                }
                expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("%")) {
                int position2 = temp.indexOf("%");  //找到%的位置
                int position1 = 0;  //找到数的位置
                if (position2 == 0) {
                    throw new ComputeError("请确认%前存在数字");
                }
                else if (temp.charAt(position2 - 1) < '0' || temp.charAt(position2 - 1) > '9'
                        || temp.charAt(position2 - 1) == '.') {
                    throw new ComputeError("请确认%前存在数字");
                }
                if (position2 + 1 != temp.length() && temp.charAt(position2 + 1) >= '0' && temp.charAt(position2 + 1) <= '9') {
                    throw new ComputeError("请确认%右边的写法是否合法");
                }
                for (int i = position2 - 1; i >= 0; i--) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认小数格式正确");
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position1 = i + 1;
                        break;
                    }
                    position1 = i;  //如果向前没找到其他符号，则表明数字的位置从0开始
                }
                result = Double.parseDouble(temp.substring(position1, position2)) / 100;
                temp = replace(temp, String.valueOf(result), position1, position2);
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("^")) {  //求幂函数和求根
                int position2 = temp.indexOf("^");  //找到^的位置
                int position1 = 0;  //找到数的位置
                if (position2 == 0) {
                    throw new ComputeError("请确认^前存在数字");
                }
                else if (temp.charAt(position2 - 1) < '0' || temp.charAt(position2 - 1) > '9'
                        || temp.charAt(position2 - 1) == '.') {
                    throw new ComputeError("请确认^前存在数字");
                }
                for (int i = position2 - 1; i >= 0; i--) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认小数格式正确");
                    }
                    if (temp.charAt(i) == '-' && i == 0) {
                        position1 = i;
                        break;
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position1 = i + 1;
                        break;
                    }
                    position1 = i;
                }
                int before = position1; //由于后面还会使用到position1，但position1要用于替换temp，所以先保留下来
                num1 = Double.parseDouble(temp.substring(position1, position2));    //获取底数
                dotCount = 0;
                if (position2 == temp.length() - 1) {
                    throw new ComputeError("请确认^后存在数字");
                }
                else if ((temp.charAt(position2 + 1) < '0' || temp.charAt(position2 + 1) > '9'
                        || temp.charAt(position2 + 1) == '.') && temp.charAt(position2 + 1) != '-') {
                    throw new ComputeError("请确认^后存在数字或是一个完整的数字");   //可以为负
                }
                for (int i = position2 + 1; i < temp.length(); i++) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认小数格式正确");
                    }
                    if (temp.charAt(i) == '-' && i == position2 + 1) {
                        continue;
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position1 = i - 1;  //要注意获取负数的同时排除负数后面的减运算符
                        break;
                    }
                    position1 = i;
                }
                int after = position1;
                num2 = Double.parseDouble(temp.substring(position2 + 1, position1 + 1));    //获取指数
                if ((1 / num2) % 2 == 0 && num1 < 0) {
                    throw new ComputeError("请确认指数运算的底数选择正确");
                }
                result = Math.pow(num1, num2);
                if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                        && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                    //判断一个数能否用整型代替浮点型
                    temp = replace(temp, String.valueOf((int) result), before, after);
                }
                else {
                    temp = replace(temp, String.valueOf(result), before, after);
                }
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("mod")) {    //求余，两边的数要求是整数
                int symbolPosition = temp.indexOf("mod");
                int position1 = 0;
                int position2 = 0;
                if (symbolPosition == 0) {
                    throw new ComputeError("请确认mod前有数");
                }
                else if (temp.charAt(symbolPosition - 1) < '0' || temp.charAt(symbolPosition - 1) > '9') {
                    throw new ComputeError("请确认mod前有数");
                }
                if (symbolPosition + 3 == temp.length()) {
                    throw new ComputeError("请确认mod后有数");
                }
                else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9') &&
                        temp.charAt(symbolPosition + 3) != '-') {
                    throw new ComputeError("请确认mod后有数");
                }
                for (int i = symbolPosition - 1; i >= 0; i--) {  //先求mod左边的数
                    if (temp.charAt(i) == '.') {
                        throw new ComputeError("请确认mod前的数是整数");
                    }
                    if (temp.charAt(i) == '-') {
                        dashCount++;
                    }
                    if (temp.charAt(i) < '0' || temp.charAt(i) > '9' || dashCount > 1) {
                        position1 = i + 1;
                        break;
                    }
                    position1 = i;
                }
                num1 = Integer.parseInt(temp.substring(position1, symbolPosition));
                for (int i = symbolPosition + 3; i < temp.length(); i++) {  //再求mod右边的数
                    if (temp.charAt(i) == '.') {
                        throw new ComputeError("请确认mod后的数是整数");
                    }
                    if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                        continue;
                    }
                    if (temp.charAt(i) < '0' || temp.charAt(i) > '9') {
                        position2 = i - 1;
                        break;
                    }
                    position2 = i;
                }
                num2 = Integer.parseInt(temp.substring(symbolPosition + 3, position2 + 1));
                result = num1 % num2;
                temp = replace(temp, String.valueOf((int)result), position1, position2);
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("sin")) {    //求sin和arcsin
                int symbolPosition;
                int position = 0;
                if (temp.indexOf("sin") > 2 && temp.indexOf("sin") == temp.indexOf("arcsin") + 3) {
                    //表示这里的sin其实是arcsin
                    symbolPosition = temp.indexOf("arcsin");
                    if (symbolPosition + 6 == temp.length()) {
                        throw new ComputeError("请确认arcsin后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 6) < '0' || temp.charAt(symbolPosition + 6) > '9')
                            && temp.charAt(symbolPosition + 6) != '-') {
                        throw new ComputeError("请确认arcsin后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认arcsin前的格式正确");
                    }
                    for (int i = symbolPosition + 6; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认arcsin后的小数格式正确");
                        }
                        if (i == symbolPosition + 6 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 6, position + 1));
                    if (num1 > 1 || num1 < -1) {
                        throw new ComputeError("请确认arcsin是否超出定义域");
                    }
                    result = Math.asin(num1);
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), symbolPosition, position);
                    }
                }
                else {
                    symbolPosition = temp.indexOf("sin");
                    if (symbolPosition + 3 == temp.length()) {
                        throw new ComputeError("请确认sin后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9')
                            && temp.charAt(symbolPosition + 3) != '-') {
                        throw new ComputeError("请确认sin后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认sin前的格式正确");
                    }
                    for (int i = symbolPosition + 3; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认sin后的小数格式正确");
                        }
                        if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 3, position + 1));
                    num1 = Math.toRadians(num1);
                    result = Math.sin(num1);
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), symbolPosition, position);
                    }
                }
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("cos")) {
                int symbolPosition;
                int position = 0;
                if (temp.indexOf("cos") > 2 && temp.indexOf("cos") == temp.indexOf("arccos") + 3) {
                    symbolPosition = temp.indexOf("arccos");
                    if (symbolPosition + 6 == temp.length()) {
                        throw new ComputeError("请确认arccos后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 6) < '0' || temp.charAt(symbolPosition + 6) > '9')
                            && temp.charAt(symbolPosition + 6) != '-') {
                        throw new ComputeError("请确认arccos后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认arccos前的格式正确");
                    }
                    for (int i = symbolPosition + 6; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认arccos后的小数格式正确");
                        }
                        if (i == symbolPosition + 6 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 6, position + 1));
                    if (num1 > 1 || num1 < -1) {
                        throw new ComputeError("请确认arccos是否超出定义域");
                    }
                    result = Math.acos(num1);
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), symbolPosition, position);
                    }
                }
                else {
                    symbolPosition = temp.indexOf("cos");
                    if (symbolPosition + 3 == temp.length()) {
                        throw new ComputeError("请确认cos后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9')
                            && temp.charAt(symbolPosition + 3) != '-') {
                        throw new ComputeError("请确认cos后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认cos前的格式正确");
                    }
                    for (int i = symbolPosition + 3; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认cos后的小数格式正确");
                        }
                        if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 3, position + 1));
                    num1 = Math.toRadians(num1);
                    result = Math.cos(num1);
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), symbolPosition, position);
                    }
                }
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("tan")) {
                int symbolPosition;
                int position = 0;//判断一个数能否用整型代替浮点型
                if (temp.indexOf("tan") > 2 && temp.indexOf("tan") == temp.indexOf("arctan") + 3) {
                    symbolPosition = temp.indexOf("arctan");
                    if (symbolPosition + 6 == temp.length()) {
                        throw new ComputeError("请确认arctan后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 6) < '0' || temp.charAt(symbolPosition + 6) > '9')
                            && temp.charAt(symbolPosition + 6) != '-') {
                        throw new ComputeError("请确认arctan后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认arctan前的格式正确");
                    }
                    for (int i = symbolPosition + 6; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认arctan后的小数格式正确");
                        }
                        if (i == symbolPosition + 6 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 6, position + 1));
                    result = Math.atan(num1);
                }
                else {
                    symbolPosition = temp.indexOf("tan");
                    if (symbolPosition + 3 == temp.length()) {
                        throw new ComputeError("请确认tan后有数");
                    }
                    else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9')
                            && temp.charAt(symbolPosition + 3) != '-') {
                        throw new ComputeError("请确认tan后有数");
                    }
                    if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                            && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                        throw new ComputeError("请确认tan前的格式正确");
                    }
                    for (int i = symbolPosition + 3; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认tan后的小数格式正确");
                        }
                        if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position = i - 1;
                            break;
                        }
                        position = i;
                    }
                    num1 = Double.parseDouble(temp.substring(symbolPosition + 3, position + 1));
                    if ((num1 / 90) % 2 == 1 || (num1 / 90) % 2 == -1) {
                        throw new ComputeError("请确认tan是否超出定义域");
                    }
                    num1 = Math.toRadians(num1);
                    result = Math.tan(num1);
                }
                if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                        && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                    //判断一个数能否用整型代替浮点型
                    temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                }
                else {
                    temp = replace(temp, String.valueOf(result), symbolPosition, position);
                }
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("abs")) {
                int symbolPosition = temp.indexOf("abs");
                int position = 0;
                if (symbolPosition + 3 == temp.length()) {
                    throw new ComputeError("请确认abs后有数");
                }
                else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9')
                        && temp.charAt(symbolPosition + 3) != '-') {
                    throw new ComputeError("请确认abs后有数");
                }
                if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                        && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                    throw new ComputeError("请确认abs前的格式正确");
                }
                for (int i = symbolPosition + 3; i < temp.length(); i++) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认abs后的小数格式正确");
                    }
                    if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                        continue;
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position = i - 1;
                        break;
                    }
                    position = i;
                }
                num1 = Double.parseDouble(temp.substring(symbolPosition + 3, position + 1));
                result = Math.abs(num1);
                if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                        && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                    //判断一个数能否用整型代替浮点型
                    temp = replace(temp, String.valueOf((int) result), symbolPosition, position);
                }
                else {
                    temp = replace(temp, String.valueOf(result), symbolPosition, position);
                }
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("INT")) {
                int symbolPosition = temp.indexOf("INT");
                int position = 0;
                if (symbolPosition + 3 == temp.length()) {
                    throw new ComputeError("请确认INT后有数");
                }
                else if ((temp.charAt(symbolPosition + 3) < '0' || temp.charAt(symbolPosition + 3) > '9')
                        && temp.charAt(symbolPosition + 3) != '-') {
                    throw new ComputeError("请确认INT后有数");
                }
                if (symbolPosition != 0 && ((temp.charAt(symbolPosition - 1) >= '0'
                        && temp.charAt(symbolPosition - 1) <= '9') || temp.charAt(symbolPosition - 1) == '.')) {
                    throw new ComputeError("请确认INT前的格式正确");
                }
                for (int i = symbolPosition + 3; i < temp.length(); i++) {
                    if (temp.charAt(i) == '.') {
                        dotCount++;
                    }
                    if (dotCount > 1) {
                        throw new ComputeError("请确认INT后的小数格式正确");
                    }
                    if (i == symbolPosition + 3 && temp.charAt(i) == '-') {
                        continue;
                    }
                    if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                        position = i - 1;
                        break;
                    }
                    position = i;
                }
                num1 = Double.parseDouble(temp.substring(symbolPosition + 3, position + 1));
                if (num1 < 0) {
                    num1 = num1 - 1;    //取整规则是取不大于这个数的最大整数，所以在负数区要减一
                }
                temp = replace(temp, String.valueOf((int) num1), symbolPosition, position);
                expression = replace(expression, temp,leftFixed + 1, rightFixed - 1 );
                if (digui(temp)) {
                    getResult();
                }
            }
            else if (temp.contains("×") || temp.contains("÷")) {    //乘和除的优先级相同，应考虑从左往右计算
                if (!temp.contains("÷") ||
                        (temp.contains("÷") && temp.contains("×") && temp.indexOf("×") < temp.indexOf("÷"))) {
                    //如果第一个乘号在第一个除号左边
                    int symbolPosition = temp.indexOf("×");
                    int position1 = 0;
                    int position2 = 0;
                    if (symbolPosition == 0) {  //如果×在首位
                        throw new ComputeError("请确认×前有数");
                    }
                    else if (symbolPosition == temp.length() - 1) { //如果×在最右边
                        throw new ComputeError("请确认×后有数");
                    }
                    else if (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) != '-' &&
                            (temp.charAt(symbolPosition + 1) < '0' || temp.charAt(symbolPosition + 1) > '9'
                                    || temp.charAt(symbolPosition + 1) == '.')) {
                        throw new ComputeError("请确认×右边有数或数的格式正确");
                    }
                    for (int i = symbolPosition - 1; i >= 0; i--) { //获取×前的数
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (i == symbolPosition - 1) {
                                throw new ComputeError("请确认×前有数");
                            }
                            if (temp.charAt(i) == '-' && i != 0
                                    && temp.charAt(i - 1) >= '0' && temp.charAt(i - 1) <='9') {
                                position1 = i + 1;
                            }
                            else if (temp.charAt(i) == '-' && i == 0 ) {
                                position1 = i;
                            }
                            else {
                                position1 = i + 1;
                            }
                            break;
                        }
                        position1 = i;
                    }
                    num1 = Double.parseDouble(temp.substring(position1, symbolPosition));
                    dotCount = 0;
                    for (int i = symbolPosition + 1; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (temp.charAt(symbolPosition + 1) == '-' && dashCount == 0) {
                                dashCount++;
                                continue;
                            }
                            else {
                                position2 = i - 1;
                                break;
                            }
                        }
                        position2 = i;
                    }
                    num2 = Double.parseDouble(temp.substring(symbolPosition + 1, position2 + 1));
                    result = num1 * num2;
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), position1, position2);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), position1, position2);
                    }
                    expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                    if (digui(temp)) {
                        getResult();
                    }
                }
                else if (!temp.contains("×") ||
                        (temp.contains("×") && temp.contains("÷") && temp.indexOf("÷") < temp.indexOf("×"))) {
                    //如果第一个除号在第一个乘号左边
                    int symbolPosition = temp.indexOf("÷");
                    int position1 = 0;
                    int position2 = 0;
                    if (symbolPosition == 0) {
                        throw new ComputeError("请确认÷左边有数");
                    }
                    else if (symbolPosition == temp.length() - 1) {
                        throw new ComputeError("请确认÷右边有数");
                    }
                    else if (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) != '-' &&
                            (temp.charAt(symbolPosition + 1) <= '0' || temp.charAt(symbolPosition + 1) > '9'
                                    || temp.charAt(symbolPosition + 1) == '.')) {
                        throw new ComputeError("请确认÷右边有数或数的格式正确");
                    }
                    for (int i = symbolPosition - 1; i >= 0; i--) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (i == symbolPosition - 1) {
                                throw new ComputeError("请确认÷前有数");
                            }
                            if (temp.charAt(i) == '-' && i != 0
                                    && temp.charAt(i - 1) >= '0' && temp.charAt(i - 1) <='9') {
                                position1 = i + 1;
                            }
                            else if (temp.charAt(i) == '-' && i == 0 ) {
                                position1 = i;
                            }
                            else {
                                position1 = i + 1;
                            }
                            break;
                        }
                        position1 = i;
                    }
                    num1 = Double.parseDouble(temp.substring(position1, symbolPosition));
                    dotCount = 0;
                    for (int i = symbolPosition + 1; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请检查小数格式是否正确");
                        }
                        if (i == symbolPosition + 1 && temp.charAt(i) == '-') {
                            continue;
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            position2 = i - 1;
                            break;
                        }
                        position2 = i;
                    }
                    num2 = Double.parseDouble(temp.substring(symbolPosition + 1, position2 + 1));
                    result = num1 / num2;
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), position1, position2);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), position1, position2);
                    }
                    expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                    if (digui(temp)) {
                        getResult();
                    }
                }
            }
            else if (temp.contains("+") || temp.indexOf("-", 1) != -1) {
                if (temp.indexOf("-", 1) == -1 ||
                        (temp.indexOf("-", 1) != -1
                                && temp.contains("+") && temp.indexOf("+") < temp.indexOf("-", 1))) {
                    int symbolPosition = temp.indexOf("+");
                    int position1 = 0;
                    int position2 = 0;
                    if (symbolPosition == 0) {
                        throw new ComputeError("请确认+左边有数");
                    }
                    else if (symbolPosition == temp.length() - 1) {
                        throw new ComputeError("请确认+右边有数");
                    }
                    else if (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) != '-' &&
                            (temp.charAt(symbolPosition + 1) < '0' || temp.charAt(symbolPosition + 1) > '9'
                                    || temp.charAt(symbolPosition + 1) == '.')) {
                        throw new ComputeError("请确认+右边有数或数的格式正确");
                    }
                    for (int i = symbolPosition - 1; i >= 0; i--) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数的格式是否正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (temp.charAt(i) == '-') {
                                position1 = i;
                            }
                            else {
                                position1 = i + 1;
                            }
                            break;
                        }
                        position1 = i;
                    }
                    num1 = Double.parseDouble(temp.substring(position1, symbolPosition));
                    dotCount = 0;
                    for (int i = symbolPosition + 1; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式是否正确");
                        }
                        if (temp.charAt(symbolPosition + 1) == '-' && i == symbolPosition + 1) {
                            continue;
                        }
                        else if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.'){
                            position2 = i - 1;
                            break;
                        }
                        position2 = i;
                    }
                    num2 = Double.parseDouble(temp.substring(symbolPosition + 1, position2 + 1));
                    result = num1 + num2;
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), position1, position2);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), position1, position2);
                    }
                    expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                    if (digui(temp)) {
                        getResult();
                    }
                }
                else if (!temp.contains("+") ||
                        (temp.contains("+") && temp.indexOf("-", 1) != -1
                                && temp.indexOf("-", 1) < temp.indexOf("+"))) {
                    int symbolPosition = temp.indexOf("-", 1);
                    //这里从1开始是因为有可能-是作为负号而不是运算符
                    int position1 = 0;
                    int position2 = 0;
                    if (symbolPosition == temp.length() - 1) {
                        throw new ComputeError("请确认-后有数");
                    }
                    else if (symbolPosition != temp.length() - 1 && temp.charAt(symbolPosition + 1) != '-' &&
                            (temp.charAt(symbolPosition + 1) < '0' || temp.charAt(symbolPosition + 1) > '9'
                                    || temp.charAt(symbolPosition + 1) == '.')) {
                        throw new ComputeError("请确认-右边有数或数的格式正确");
                    }
                    for (int i = symbolPosition - 1; i >= 0; i--) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (temp.charAt(i) == '-') {
                                position1 = i;
                            }
                            else {
                                position1 = i - 1;
                            }
                            break;
                        }
                        position1 = i;
                    }
                    num1 = Double.parseDouble(temp.substring(position1, symbolPosition));
                    dotCount = 0;
                    for (int i = symbolPosition + 1; i < temp.length(); i++) {
                        if (temp.charAt(i) == '.') {
                            dotCount++;
                        }
                        if (dotCount > 1) {
                            throw new ComputeError("请确认小数格式正确");
                        }
                        if ((temp.charAt(i) < '0' || temp.charAt(i) > '9') && temp.charAt(i) != '.') {
                            if (temp.charAt(symbolPosition + 1) == '-' && dashCount == 0) {
                                dashCount++;
                                continue;
                            }
                            else {
                                position2 = i - 1;
                                break;
                            }
                        }
                        position2 = i;
                    }
                    num2 = Double.parseDouble(temp.substring(symbolPosition + 1, position2 + 1));
                    result = num1 - num2;
                    if (String.valueOf(result).substring(String.valueOf(result).indexOf(".") + 1).length() == 1
                            && String.valueOf(result).charAt(String.valueOf(result).indexOf(".") + 1) == '0') {
                        //判断一个数能否用整型代替浮点型
                        temp = replace(temp, String.valueOf((int) result), position1, position2);
                    }
                    else {
                        temp = replace(temp, String.valueOf(result), position1, position2);
                    }
                    expression = replace(expression, temp, leftFixed + 1, rightFixed - 1);
                    if (digui(temp)) {
                        getResult();
                    }
                }
            }
            //以下部分是去括号
            if (expression.contains("(")) {
                leftFixed = expression.indexOf("(");
                leftNotFixed = expression.indexOf("(", leftFixed + 1);
                rightFixed = expression.indexOf(")", leftFixed);
                while (leftNotFixed != -1 && rightFixed > leftFixed) {
                    leftFixed = expression.indexOf("(", leftFixed + 1);
                    leftNotFixed = expression.indexOf("(", leftFixed + 1);
                    rightFixed = expression.indexOf(")", leftFixed);
                }
                expression = replace(expression, temp, leftFixed, rightFixed);
                //在计算完temp里的表达式后，要将最里层括号去掉，但因为修改过了expression的值，所以要重新定位括号位置
            }
        }
        if (expression.contains("(")) {
            getResult();    //如果expression中还有括号，则寻找下一对括号计算括号内表达式
        }
        return expression;
    }
    public int jieCheng (int num) { //计算阶乘
        int n = 1;
        int sum = 1;
        while (n <= num) {
            sum = n * sum;
            n++;
        }
        return sum;
    }
    public String replace (String s1, String s2, int position1, int position2) {   //用s2替换s1中的一部分
        String s;
        if (position1 == 0 && position2 + 1 >= s1.length()) {
            s = s2;
        }
        else if (position1 == 0) {
            s = s2 + s1.substring(position2 + 1);
        }
        else if (position2 + 1 >= s1.length()) {
            s = s1.substring(0, position1) + s2;
        }
        else {
            s = s1.substring(0, position1) + s2 + s1.substring(position2 + 1);
        }
        return s;
    }
    public boolean digui (String s) {   //判断是否要进行递归
        if (s.charAt(0) == '-') {    //当第一个数是负数时
            for (int i = 1; i < s.length(); i++) {
                if ((s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != '.') {
                    return true;  //如果出现其他运算符，则进行递归
                }
            }
        }
        else {  //当第一个数不是负数时
            for (int i = 0; i < s.length(); i++) {
                if ((s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != '.') {
                    return true;  //如果出现其他运算符，则进行递归
                }
            }
        }
        return false;
    }
}