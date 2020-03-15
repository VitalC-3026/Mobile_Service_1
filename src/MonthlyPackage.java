import java.io.BufferedWriter;
import java.io.IOException;

public abstract class MonthlyPackage {
    char PackageNumber;
    String PackageName;
    double phone_call = 0;
    double extra_call = 0;
    double pack_call = 0;
    double data = 0;
    double extra_data = 0;
    double pack_data = 0;
    int message = 0;
    int extra_message = 0;
    int pack_message = 0;
    double fee = 0;
    double extra_fee = 0;
    double pack_fee = 0;
    public void printOutPackage(){
        System.out.println("--------------------");
        System.out.println("超出套餐的计费:");
        System.out.println("通话:0.2元/分钟");
        System.out.println("上网流量:0.1元/MB");
        System.out.println("短信:0.1元/条");
    }
    public void printOutToText(BufferedWriter bw) throws IOException {
        String[] packageInfo = {"超出套餐的计费:\n","通话:0.2元/分钟\n","上网流量:0.1元/MB\n",
                "短信:0.1元/条\n","——————————————————————————————————\n"};
        for(int i = 0; i < packageInfo.length; i++){
            bw.append(packageInfo[i]);
            bw.flush();
        }
    }
    public abstract void printPackage();
    public abstract void printPackageUse(SimCard s);
    public abstract void printPackageLeft(SimCard s);
    public abstract void DeleteAllInfo();
    //public abstract void Call(double time);
    //public abstract void Text(int message);
}
