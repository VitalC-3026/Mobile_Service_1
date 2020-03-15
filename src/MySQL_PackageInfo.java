import com.mysql.cj.jdbc.exceptions.MySQLQueryInterruptedException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.LinkedList;

public class MySQL_PackageInfo extends BaseDAO implements PackageInfoDAO {

    boolean modified = true;

    void PackageInformation(File f) throws Exception{
        try{
            stmt = getConnection();
            if(true){
                CreatePackageInformation(f);
            }
            else{
                UpdatePackageInformation(f);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //rs.close();
        stmt.close();
        closeAll();
    }

    @Override
    public void CreatePackageInformation(File f) throws Exception {
        try {
            FileReader fr = new FileReader("资费说明.txt");
            BufferedReader br = new BufferedReader(fr);

            boolean flag = true;
            String string = null, packageName = null, data = null, message = null, fee = null, call = null;
            StringBuffer stringBuffer = null;
            while((string = br.readLine())!=null){
                if(string.contains("套餐类型:")){
                    int comma = string.indexOf(":");
                    packageName = string.substring(comma+1);
                    stringBuffer = new StringBuffer("insert into packageInfo(packageName, phoneCall, data, message, fee) values('");
                    stringBuffer.append(packageName + "',");
                    flag = true;
                }
                if(string.contains("超出套餐")){
                    packageName = "超出套餐计费:";
                    stringBuffer = new StringBuffer("insert into packageInfo(packageName, phoneCall, data, message, fee) values('");
                    stringBuffer.append(packageName + "',");
                }
                if(string.contains("普通计费")){
                    packageName = "普通计费";
                    stringBuffer = new StringBuffer("insert into packageInfo(packageName, phoneCall, data, message, fee) values('");
                    stringBuffer.append(packageName + "',");
                }
                if(string.contains("通话时长:")){
                    int comma = string.indexOf(":");
                    int time = string.indexOf("分钟");
                    call = string.substring(comma+1,time);
                    stringBuffer.append(Double.valueOf(call)+",");
                }
                if(string.contains("上网流量:")){
                    int comma = string.indexOf(":");
                    int gb = string.lastIndexOf("GB");
                    data = string.substring(comma+1,gb);
                    stringBuffer.append(Double.parseDouble(data)*1024+",");
                }
                if(string.contains("短信条数:")){
                    int comma = string.indexOf(":");
                    int num = string.lastIndexOf("条");
                    message = string.substring(comma+1, num);
                    stringBuffer.append(Integer.valueOf(message)+",");
                }
                if(string.contains("月资费:")){
                    int comma=string.indexOf(":");
                    int yuan = string.indexOf("元");
                    fee = string.substring(comma+1, yuan);
                    stringBuffer.append(Double.valueOf(fee));
                }
                if(string.contains("————————————")){
                    try {
                        StringBuffer duplicate = new StringBuffer(") on duplicate key update phoneCall = " + Double.valueOf(call)+
                                ", data = "+Double.parseDouble(data)*1024+", message = "+Integer.valueOf(message)+
                                ", fee = "+Double.valueOf(fee)+";");
                        stringBuffer.append(duplicate);
                        stmt.executeUpdate(stringBuffer.toString());
                    }catch(MySQLQueryInterruptedException sqle){
                        sqle.printStackTrace();
                        sqle.getLocalizedMessage();//??
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    stringBuffer.delete(0,stringBuffer.length());
                }

            }
            if(stringBuffer.length()>0){
                try {

                    StringBuffer duplicate = new StringBuffer(") on duplicate key update phoneCall = " + Double.valueOf(call)+
                            ", data = "+Double.parseDouble(data)*1024+", message = "+Integer.valueOf(message)+
                            ", fee = "+Double.valueOf(fee)+";");
                    stringBuffer.append(duplicate);
                    //System.out.println(stringBuffer.toString());
                    stmt.executeUpdate(stringBuffer.toString());
                }catch(MySQLQueryInterruptedException sqle){
                    sqle.printStackTrace();
                    sqle.getLocalizedMessage();//??
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(FileNotFoundException fe){
            System.out.println("File doesn't exist!");
            fe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        modified = true;
    }

    @Override
    public void UpdatePackageInformation(File f) throws Exception {
        try {
            FileReader fr = new FileReader("资费说明.txt");
            BufferedReader br = new BufferedReader(fr);
            stmt = getConnection();
            String string, packageName = null, data, message, fee, call;
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer pre = new StringBuffer("Update packageInfo SET ");
            double ditemp; int itemp;
            while((string = br.readLine())!=null){
                if(string.contains("套餐类型:")){
                    int comma = string.indexOf(":");
                    packageName = string.substring(comma+1);
                    stringBuffer.append(pre);
                    stringBuffer.append("packageName = '" + packageName + "',");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("超出套餐")){
                    packageName = "超出套餐的计费";
                    stringBuffer.append(pre);
                    stringBuffer.append("packageName = '" + packageName + "',");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("普通计费")){
                    packageName = "普通计费";
                    stringBuffer.append(pre);
                    stringBuffer.append("packageName = '" + packageName + "',");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("通话时长:")){
                    int comma = string.indexOf(":");
                    int time = string.indexOf("分钟");
                    call = string.substring(comma+1,time);
                    stringBuffer.append("phoneCall = " + Double.valueOf(call) + ",");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("上网流量:")){
                    int comma = string.indexOf(":");
                    int gb = string.indexOf("GB");
                    data = string.substring(comma+1,gb);
                    stringBuffer.append("data = " + Double.valueOf(data)*1024 +",");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("短信条数:")){
                    int comma = string.indexOf(":");
                    int num = string.lastIndexOf("条");
                    message = string.substring(comma+1,num);
                    stringBuffer.append("message = " + Integer.valueOf(message) +",");
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("月资费:")){
                    int comma=string.indexOf(":");
                    int yuan = string.indexOf("元");
                    fee = string.substring(comma+1, yuan);
                    stringBuffer.append("fee = "+Double.valueOf(fee));
                    System.out.println(stringBuffer.toString());
                }
                if(string.contains("————————————")){
                    try {
                        stringBuffer.append(" WHERE packageName = " + "'" + packageName + "'");
                        System.out.println(stringBuffer.toString());
                        stmt.executeUpdate(stringBuffer.toString());
                    }catch(MySQLQueryInterruptedException sqle){
                        sqle.printStackTrace();
                        sqle.getLocalizedMessage();//??
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    stringBuffer.delete(0,stringBuffer.length());
                }
            }
            if(stringBuffer.length()>0){
                try {
                    stringBuffer.append(" WHERE packageName = " + "'").append(packageName).append("'");
                    System.out.println(stringBuffer.toString());
                    stmt.executeUpdate(stringBuffer.toString());
                }catch(MySQLQueryInterruptedException sqle){
                    sqle.printStackTrace();
                    sqle.getLocalizedMessage();//??
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(FileNotFoundException fe){
            System.out.println("File doesn't exist!");
            fe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        modified = true;
    }

    @Override
    public void ModifyPackageInformation(MonthlyPackage p) throws Exception{

        stmt = getConnection();
        StringBuffer packageInfo = new StringBuffer("SELECT * FROM packageInfo WHERE PackageName = '"+p.PackageName+"'");
        try {
            rs = stmt.executeQuery(packageInfo.toString());
            int itemp;
            double ditemp;
            String packageName;
            while (rs.next()) {
                packageName = rs.getString("packageName");
                if(packageName.equals("超出套餐的计费")){
                    continue;
                }
                ditemp = rs.getDouble("phoneCall");
                p.pack_call = ditemp;
                ditemp = rs.getDouble("data");
                p.pack_data = ditemp;
                ditemp = rs.getDouble("fee");
                p.fee = ditemp;
                p.pack_fee = ditemp;
                itemp = rs.getInt("message");
                p.pack_message = itemp;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        rs.close();
        stmt.close();
        closeAll();



    }

    @Override
    public void UpdatePackageInformation(SimCard s) throws Exception {
        try {
            /*TalkingTooMuch talkingTooMuch = new TalkingTooMuch();
            NetWorm netWorm = new NetWorm();
            Superman superman = new Superman();
            NormalPackage normalPackage = new NormalPackage();*/
            stmt = getConnection();
            StringBuffer updatePackInfo = new StringBuffer("UPDATE packageUse SET packageName = ?, phoneCall = ?, OutPhoneCall = ?,");
            updatePackInfo.append("data = ?, OutData = ?, message = ?, OutMessage = ?, fee = ?, OutFee = ?");
            updatePackInfo.append("WHERE Telephone = '" + s.telephone + "'");
            Object[] packInfo = new Object[9];
            packInfo[0] = s.getPackageNumber().PackageName;
            packInfo[1] = s.getPackageNumber().phone_call;
            packInfo[2] = s.getPackageNumber().extra_call;
            packInfo[3] = s.getPackageNumber().data;
            packInfo[4] = s.getPackageNumber().extra_data;
            packInfo[5] = s.getPackageNumber().message;
            packInfo[6] = s.getPackageNumber().extra_message;
            packInfo[7] = s.getPackageNumber().fee;
            packInfo[8] = s.getPackageNumber().fee - s.getPackageNumber().pack_fee;
            executeUpdate(updatePackInfo.toString(), packInfo);
            /*StringBuffer updateBalance = new StringBuffer("UPDATE userInfo SET balance = ? WHERE Telephone = ?");
            Object[] balance = {s.getBalance()};
            util.executeUpdate(updateBalance.toString(), balance);*/

        }catch (Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }


    @Override
    public LinkedList PrintOutPackageInformation(SimCard s) throws Exception {
        stmt = getConnection();
        rs = null;
        LinkedList<String> packInfo = new LinkedList<>();
        try {
            StringBuffer packageInfo = new StringBuffer("SELECT * FROM packageUse WHERE Telephone = '" + s.telephone + "'");
            rs = stmt.executeQuery(packageInfo.toString());
            String temp;
            double dtemp;
            int itemp;
            while (rs.next()) {
                temp = rs.getString("Telephone");
                temp += ": ";
                packInfo.add(temp);
                temp = "packageName: ";
                temp += rs.getString("packageName");
                packInfo.add(temp);
                temp = "phoneCall: ";
                dtemp = rs.getDouble("phoneCall");
                temp += String.valueOf(dtemp);
                packInfo.add(temp);
                temp = "OutPhoneCall: ";
                dtemp = rs.getDouble("OutPhoneCall");
                temp += String.valueOf(dtemp);
                packInfo.add(temp);
                temp = "data: ";
                dtemp = rs.getDouble("data");
                temp += String.valueOf(dtemp);
                packInfo.add(temp);
                temp = "OutData: ";
                dtemp = rs.getDouble("OutData");
                temp += String.valueOf(dtemp);
                packInfo.add(temp);
                temp = "message: ";
                itemp = rs.getInt("message");
                temp += String.valueOf(itemp);
                packInfo.add(temp);
                temp = "OutMessage: ";
                itemp = rs.getInt("OutMessage");
                temp += String.valueOf(itemp);
                packInfo.add(temp);
                temp = "fee: ";
                dtemp = rs.getDouble("fee");
                temp += dtemp;
                packInfo.add(temp);
                temp = "OutFee: ";
                dtemp = rs.getDouble("OutFee");
                temp += dtemp;
                packInfo.add(temp);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        rs.close();
        stmt.close();
        closeAll();
        return packInfo;
    }

    @Override
    public void UpdateUserPackage(SimCard s) throws Exception {
        try{
            stmt = getConnection();
            StringBuffer curr_pack = new StringBuffer("SELECT * FROM packageUse WHERE Telephone = '"+s.getTelephone()+"'");
            rs = stmt.executeQuery(curr_pack.toString());
            while(rs.next()){
                s.monthlyPackage.fee = rs.getDouble("fee");
                s.monthlyPackage.extra_fee = rs.getDouble("OutFee");
                s.monthlyPackage.phone_call = rs.getDouble("phoneCall");
                s.monthlyPackage.extra_call = rs.getDouble("OutPhoneCall");
                s.monthlyPackage.data = rs.getDouble("data");
                s.monthlyPackage.extra_data = rs.getDouble("OutData");
                s.monthlyPackage.message = rs.getInt("message");
                s.monthlyPackage.extra_message = rs.getInt("OutMessage");
            }
        }catch(Exception e){
            e.getMessage();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public void UpdateLifeService(SimCard s, String life, double cost) throws Exception {
        try{
            stmt = getConnection();
            StringBuffer life_service = new StringBuffer("UPDATE userInfo SET lifeService = ?, balance = ? WHERE Telephone = '"+s.telephone+"'");
            //String scost = String.valueOf(cost);
            Object[] service = { life, cost};
            executeUpdate(life_service.toString(),service);
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public  void UpdateBalance(SimCard s, double d)throws Exception{
        try{
            stmt = getConnection();
            StringBuffer B = new StringBuffer("UPDATE userInfo SET balance = "+d+"WHERE Telephone = "+ s.telephone);
            stmt.executeUpdate(B.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public void ChangePackage(SimCard s, String packageName) throws Exception {
        try{
            stmt = getConnection();
            StringBuffer changeName = new StringBuffer("UPDATE userInfo SET packageName = '"+packageName+"' WHERE Telephone = '"+s.telephone+"'");
            stmt.executeUpdate(changeName.toString());
            StringBuffer packageUse = new StringBuffer("DELETE FROM packageUse WHERE Telephone = '"+s.telephone+"'");
            stmt.executeUpdate(packageUse.toString());
            StringBuffer changeCusPack = new StringBuffer("insert into packageUse(Telephone, packageName, phoneCall, OutPhoneCall, data, OutData, message, OutMessage, fee, OutFee)" +
                    " values('" + s.telephone + "','" + s.monthlyPackage.PackageName + "'," + s.monthlyPackage.phone_call + "," + s.monthlyPackage.extra_call + "," + s.monthlyPackage.data + ","
                    + s.monthlyPackage.extra_data + "," + s.monthlyPackage.message + "," + s.monthlyPackage.extra_message + "," + s.monthlyPackage.fee + "," + s.monthlyPackage.extra_fee + ")");
            stmt.executeUpdate(changeCusPack.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }
}
