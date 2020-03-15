import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Calendar;

public class MySQL_ServiceHall extends BaseDAO implements ServiceHallDAO {
    /*DatabaseUtil util = new DatabaseUtil();
    Statement stmt = null;*/
    Calendar now = Calendar.getInstance();
    MySQL_ServiceHall() throws Exception {
        //init();
    }
    public void init() throws Exception {
        stmt = getConnection();
        try{
            StringBuffer packageInfo = new StringBuffer("create table if not exists packageInfo(packageName varchar(100) PRIMARY KEY, phoneCall double," +
                    "data double, message int, fee double)");
            stmt.executeUpdate(packageInfo.toString());
            StringBuffer packageUse = new StringBuffer("create table if not exists packageUse(Telephone varchar(100) PRIMARY KEY, packageName varchar(100), phoneCall double," +
                    "OutPhoneCall double, data double, OutData double, message int, OutMessage int, fee double, OutFee double)") ;
            stmt.executeUpdate(packageUse.toString());
            StringBuffer userInfo = new StringBuffer("create table if not exists userInfo(Telephone varchar(100) PRIMARY KEY, userName varchar(100), password varchar(80))" +
                    "packageName varchar(100), balance double, lifeService varchar(100))");
            stmt.executeUpdate(userInfo.toString());
            StringBuffer deposit = new StringBuffer("create table if not exists DepositRecord(No bigint PRIMARY KEY AUTO_INCREMENT, Telephone varchar(100), Money double, DepositTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            stmt.executeUpdate(deposit.toString());
            StringBuffer Consumption = new StringBuffer("create table if not exists ConsumptionRecord(No bigint PRIMARY KEY AUTO_INCREMENT, Telephone varchar(100)), Type varchar(20), Flow double, ConsumeTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            stmt.executeUpdate(Consumption.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }


    //有一些初始化用户，通过set先行加入到customer中，然后将customer清空，每一次都从customer那里读取数据
    @Override
    public void CreateNewCustomer(SimCard s) throws Exception {
        try {
            stmt = getConnection();
            String addNewCus = "insert into userInfo(Telephone, userName, password, packageName, balance, lifeService)" +
                    " values('" + s.telephone + "','" + s.Username + "','" + s.password + "','" + s.monthlyPackage.PackageName + "'," + s.balance + ",'" + s.life_service.toString() + "')";
            addNewCus += "on duplicate key update userName = '" + s.Username + "', password = '" + s.password + "', packageName = '" + s.monthlyPackage.PackageName + "', balance = " + s.balance + ", lifeService = '" + s.life_service.toString()+"';";
            stmt.executeUpdate(addNewCus);
            //System.out.println(addNewCus);
            StringBuffer addCusPack = new StringBuffer("insert into packageUse(Telephone, packageName, phoneCall, OutPhoneCall, data, OutData, message, OutMessage, fee, OutFee)" +
                    " values('" + s.telephone + "','" + s.monthlyPackage.PackageName + "'," + s.monthlyPackage.phone_call + "," + s.monthlyPackage.extra_call + "," + s.monthlyPackage.data + ","
                    + s.monthlyPackage.extra_data + "," + s.monthlyPackage.message + "," + s.monthlyPackage.extra_message + "," + s.monthlyPackage.fee + "," + s.monthlyPackage.extra_fee + ")");
            StringBuffer duplicate2 = new StringBuffer("on duplicate key update packageName = '"+s.monthlyPackage.PackageName+"',phoneCall = " +s.monthlyPackage.phone_call+",OutPhoneCall = "+s.monthlyPackage.extra_call+",data = "+s.monthlyPackage.data+",OutData = "+
                    s.monthlyPackage.extra_data+",message = "+s.monthlyPackage.message+",OutMessage = "+s.monthlyPackage.extra_message+",fee = "+s.monthlyPackage.fee+",OutFee = "+s.monthlyPackage.extra_fee );
            //System.out.println(addCusPack);
            addCusPack.append(duplicate2);
            stmt.executeUpdate(addCusPack.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public void UpdateDeposit(SimCard s, String deposit) throws Exception {
        int first = deposit.indexOf("值");
        int last = deposit.indexOf("元");
        double d = Double.parseDouble(deposit.substring(first+1,last));
        try {
            stmt = getConnection();
            String depositRecord = new String("insert into DepositRecord(Telephone, Money) values('" + s.telephone + "'," + d + ")");
            stmt.executeUpdate(depositRecord);
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public void UpdateConsumption(SimCard s, String consume) throws SQLException {
        String type,consumeRecord;
        int iflow;
        double dflow;
        if(consume.contains("通话")){
            int first = consume.indexOf("话");
            int last = consume.indexOf("分");
            dflow = Double.parseDouble(consume.substring(first+2,last));
            type = "通话";
            consumeRecord =  new String("insert into ConsumptionRecord(Telephone, Type, Flow) values('" + s.telephone + "','" + type+"',"+dflow + ")");
        }
        else if(consume.contains("短信")){
            type="短信";
            int first = consume.indexOf("信");
            int last = consume.indexOf("条");
            iflow = Integer.parseInt(consume.substring(first+2,last));
            consumeRecord = new String("insert into ConsumptionRecord(Telephone, Type, Flow) values('" + s.telephone + "','" + type+"',"+iflow + ")");
        }
            else{
                type="流量";
                int first = consume.indexOf("量");
                int last = consume.indexOf("M");
                dflow = Double.parseDouble(consume.substring(first+2, last));
            consumeRecord =  new String("insert into ConsumptionRecord(Telephone, Type, Flow) values('" + s.telephone + "','" + type+"',"+dflow + ")");

        }
        try {
            stmt = getConnection();
            stmt.executeUpdate(consumeRecord);
        }catch(Exception e){
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public void Delete(String s) throws SQLException {
        try{
            stmt = getConnection();


            String[] table = {"packageUse","userInfo","ConsumptionRecord","DepositRecord"};
            for(String t: table){
                String preS = "DELETE FROM ";
                preS += t;
                preS += " WHERE Telephone = '" + s +"'";
                System.out.println(preS);
                stmt.executeUpdate(preS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        stmt.close();
        closeAll();
    }

    @Override
    public boolean FindCustomer(String phone) throws Exception{
        ResultSet rs = null;
        try{
            stmt = getConnection();
            StringBuffer find = new StringBuffer("SELECT * FROM userInfo WHERE Telephone = '"+phone+"'");
            rs = stmt.executeQuery(find.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        while(rs.next()){
            String tele = rs.getString("Telephone");
            if(tele.equals(phone)){
                rs.close();
                stmt.close();
                return true;
            }

        }
        rs.close();
        stmt.close();
        return false;
    }

    //验证用户是否能登录系统，并根据权限返回用户信息
    @Override
    public SimCard Customer_info(String phone, String password) throws Exception {
        ResultSet rs1 = null, rs2 = null;
        boolean qualified = false;
        String packageName = null,lifeService = null;
        Double balance = 0.0, fee = 0.0, outFee = 0.0, call = 0.0, outCall = 0.0, data = 0.0, outData = 0.0;
        int message = 0, outMessage = 0;
        if(FindCustomer(phone)){
            try{
                stmt = getConnection();
                StringBuffer p1 = new StringBuffer("SELECT password, packageName, balance, lifeService FROM userInfo WHERE Telephone = '"+phone+"'");
                rs1 = stmt.executeQuery(p1.toString());
                while(rs1.next()){
                    if(password.equals(rs1.getString("password"))){
                        qualified = true;
                    }
                    if(qualified) {
                        balance = rs1.getDouble("balance");
                        packageName = rs1.getString("packageName");
                        lifeService = rs1.getString("lifeService");

                    }
                }

                StringBuffer p2 = new StringBuffer("SELECT * FROM packageUse WHERE Telephone = '"+phone+"'");
                rs2 = stmt.executeQuery(p2.toString());

                if(qualified){
                    while(rs2.next()){
                        fee = rs2.getDouble("fee");
                        outFee = rs2.getDouble("OutFee");
                        call = rs2.getDouble("phoneCall");
                        outCall = rs2.getDouble("OutPhoneCall");
                        data = rs2.getDouble("data");
                        outData = rs2.getDouble("OutData");
                        message = rs2.getInt("message");
                        outMessage = rs2.getInt("OutMessage");
                    }
                    SimCard s = new SimCard();
                    s.telephone = phone;
                    s.balance = balance;
                    assert packageName != null;
                    switch(packageName){
                        case"超人套餐":{
                            s.monthlyPackage = new Superman();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"网虫套餐":{
                            s.monthlyPackage = new NetWorm();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"话痨套餐":{
                            s.monthlyPackage = new TalkingTooMuch();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"普通计费":{
                            s.monthlyPackage = new NormalPackage();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                    }
                    return s;
                }
                else{
                    return null;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //根据用户的电话号码进行查询，若存在则返回用户当前在数据库中的套餐使用情况的信息到用户对应的实例化对象中
    @Override
    public SimCard Customer_use(String phone) throws Exception {
        ResultSet rs1 = null, rs2 = null;
        Double balance = 0.0, fee = 0.0, outFee = 0.0, call = 0.0, outCall = 0.0, data = 0.0, outData = 0.0;
        int message = 0, outMessage = 0;
        String packageName = null;
        if(FindCustomer(phone)){
            try{
                stmt = getConnection();
                StringBuffer p1 = new StringBuffer("SELECT packageName, balance FROM userInfo WHERE Telephone = '"+phone+"'");
                rs1 = stmt.executeQuery(p1.toString());
                while(rs1.next()){
                    balance = rs1.getDouble("balance");
                    packageName = rs1.getString("packageName");
                }
                StringBuffer p2 = new StringBuffer("SELECT * FROM packageUse WHERE Telephone = '"+phone+"'");
                rs2 = stmt.executeQuery(p2.toString());

                while(rs2.next()){
                    fee = rs2.getDouble("fee");
                    outFee = rs2.getDouble("outFee");
                    call = rs2.getDouble("phoneCall");
                    outCall = rs2.getDouble("OutPhoneCall");
                    data = rs2.getDouble("data");
                    outData = rs2.getDouble("OutData");
                    message = rs2.getInt("message");
                    outMessage = rs2.getInt("OutMessage");
                }
                    SimCard s = new SimCard();
                    s.telephone = phone;
                    s.balance = balance;
                    assert packageName != null;
                    switch(packageName){
                        case"超人套餐":{
                            s.monthlyPackage = new Superman();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"网虫套餐":{
                            s.monthlyPackage = new NetWorm();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"话痨套餐":{
                            s.monthlyPackage = new TalkingTooMuch();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                        case"普通计费":{
                            s.monthlyPackage = new NormalPackage();
                            setMonthlyPackage(fee, outFee, call, outCall, data, outData, message, outMessage, s);
                            break;
                        }
                    }
                    return s;

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private void setMonthlyPackage(Double fee, Double outFee, Double call, Double outCall, Double data, Double outData, int message, int outMessage, SimCard s) {
        s.monthlyPackage.fee = fee;
        s.monthlyPackage.extra_fee = outFee;
        s.monthlyPackage.phone_call = call;
        s.monthlyPackage.extra_call = outCall;
        s.monthlyPackage.data = data;
        s.monthlyPackage.extra_data = outData;
        s.monthlyPackage.message = message;
        s.monthlyPackage.extra_message = outMessage;
    }

    @Override
    public LinkedList<String> PrintOutMonthlyConsumption(SimCard s) throws SQLException {
        LinkedList<String> results = new LinkedList<>();
        try{
            stmt = getConnection();
            StringBuffer printCon = new StringBuffer("SELECT * FROM ConsumptionRecord where Telephone = ?");
            String[] telephone = {s.telephone};
            rs = executeQuery(printCon.toString(), telephone);
            String temp, date, type, t_month;
            if(rs == null){
                return null;
            }
            while(rs.next()){
                temp = rs.getString("Telephone");
                temp += ": ";
                temp += rs.getString("Type");
                type = rs.getString("Type");
                temp += rs.getDouble("Flow");
                switch (type){
                    case"通话":
                        temp+="分钟 ";
                        break;
                    case"短信":
                        temp+="条 ";
                        break;
                    case"流量":
                        temp+="MB ";
                        break;
                }
                date = rs.getString("ConsumeTime");
                int month = now.get(Calendar.MONTH)+1;
                int hyphen1 = date.indexOf("-"), hyphen2 = date.lastIndexOf("-");
                t_month = date.substring(hyphen1+1,hyphen2);
                if(t_month.equals(String.valueOf(month))){
                    temp += date;
                    results.add(temp);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        rs.close();
        stmt.close();
        closeAll();
        return results;
    }

    @Override
    public LinkedList<String> PrintOutMonthlyDeposit(SimCard s) throws SQLException {
        LinkedList<String> results = new LinkedList<>();
        try{
            stmt = getConnection();
            StringBuffer printCon = new StringBuffer("SELECT * FROM DepositRecord where Telephone = ?");
            String[] telephone = {s.telephone};
            rs = executeQuery(printCon.toString(), telephone);
            String temp, date, temp_month;
            while(rs.next()){
                temp = rs.getString("Telephone");
                temp += ": ";
                temp += rs.getString("Money");
                temp+="元 ";
                date = rs.getString("DepositTime");
                int month = now.get(Calendar.MONTH)+1;
                int hyphen1 = date.indexOf("-"), hyphen2 = date.lastIndexOf("-");
                temp_month = date.substring(hyphen1+1,hyphen2);
                if(temp_month.equals(String.valueOf(month))){
                    temp += date;
                    results.add(temp);
                }

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        assert rs != null;
        rs.close();
        stmt.close();
        closeAll();
        return results;
    }
}
