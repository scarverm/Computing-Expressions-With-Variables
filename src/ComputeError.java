import javax.swing.*;

public class ComputeError extends Exception{
    String message;
    CalculateView cal;
    public void setView(CalculateView cal) {
        this.cal = cal;
    }
    public ComputeError(String message) {
        this.message = message;
    }
    public void showMess() {
        JOptionPane.showMessageDialog(cal, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}
