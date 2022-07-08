package model.tableModel;

public class IncomeTM {
    private String date;
    private String time;
    private Double income;

    public IncomeTM() { }

    public IncomeTM(String date, String time, Double income) {
        this.setDate(date);
        this.setTime(time);
        this.setIncome(income);
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Double getIncome() {
        return income;
    }
    public void setIncome(Double income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "IncomeTM{" +
                "date='" + getDate() + '\'' +
                ", time='" + getTime() + '\'' +
                ", income=" + getIncome() +
                '}';
    }
}
