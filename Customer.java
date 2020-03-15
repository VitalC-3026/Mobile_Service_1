import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.TimerTask;

public class Customer extends TimerTask {
    MySQL_ServiceHall serviceHall = new MySQL_ServiceHall();

    private Timer timer;
    boolean stop = false;
    private Map<String, SimCard> all_customers = new HashMap<>();
    private CycQueue<Object[]> consumption = new CycQueue<>();
    private CycQueue<Object[]> deposit = new CycQueue<>();
    private Map<String, LinkedList<String>> cconsumption = new HashMap<>();
    private Map<String, LinkedList<String>> ddeposit = new HashMap<String, LinkedList<String>>();

    Customer() throws Exception {
        initializeCustomers();
        //this.timer = timer;
    }

    private void initializeCustomers() throws Exception {
        NetWorm netWorm = new NetWorm();
        TalkingTooMuch talkingTooMuch = new TalkingTooMuch();
        Superman superman = new Superman();
        SimCard[] init_set = {
                new SimCard("Little_Wheat","152130","18902810777",12.5,talkingTooMuch),
                new SimCard("Dududu","152126","18318888999",140.7,netWorm),
                new SimCard("Lydia","152127","18902285145",62.50,netWorm),
                new SimCard("Xuan","152137","15815670706",0,superman)
        };

        add_customer(init_set[0]);
        add_customer(init_set[1]);
        add_customer(init_set[2]);
        add_customer(init_set[3]);
        serviceHall.CreateNewCustomer(init_set[0]);
        serviceHall.CreateNewCustomer(init_set[1]);
        serviceHall.CreateNewCustomer(init_set[2]);
        serviceHall.CreateNewCustomer(init_set[3]);
    }
    public void add_customer(SimCard new_cus) throws Exception {
        serviceHall.CreateNewCustomer(new_cus);
        /*all_customers.put(new_cus.getTelephone(),new_cus);
        String de = "您还没有充值";
        LinkedList<String> linkedList1 = new LinkedList<String>();
        linkedList1.add(de);
        deposit.put(new_cus.getTelephone(), linkedList1);
        String con = "您还没有消费";
        LinkedList<String> linkedList2 = new LinkedList<String>();
        linkedList2.add(con);
        consumption.put(new_cus.getTelephone(),linkedList2);*/
    }
    public void add_deposit(SimCard cus, String de) throws MyException {
            Object[] depositRecord = {cus,de};
            boolean res = deposit.EnQueue(depositRecord);
            if(!res){
                System.out.println("CycQueue_break");
                throw new MyException("CycQueue_break");
            }
            /*LinkedList<String> temp;
            temp = deposit.get(cus.getTelephone());
            if(temp.getFirst().equals("您还没有充值")){
                temp.remove("您还没有充值");
                temp.add(de);
                deposit.put(cus.getTelephone(),temp);
            }
            else{
                temp.add(de);
                deposit.put(cus.getTelephone(),temp);
            }*/
    }
    public void add_consumption(SimCard s, String con) throws MyException {
        Object[] consumeRecord = {s,con};
        boolean res = consumption.EnQueue(consumeRecord);
        if(!res){
            System.out.println("CycQueue_break");
            throw new MyException("CycQueue_break");
        }
        /*LinkedList<String> temp;
        temp = consumption.get(s.getTelephone());
        if(temp.getFirst().equals("您还没有消费")){
            temp.remove("您还没有消费");
            temp.add(con);
            consumption.put(s.getTelephone(),temp);
        }
        else{
            temp.add(con);
            consumption.put(s.getTelephone(),temp);
        }*/

    }
    @Override
    public void run(){
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(date.toString());
        while(deposit.QueueLength()>0){
            try {
                Object[] res = deposit.DeQueue();
                SimCard s = (SimCard) res[0];
                String de = (String) res[1];
                serviceHall.UpdateDeposit(s,de);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(consumption.QueueLength()>0){
            try {
                Object[] res = consumption.DeQueue();
                SimCard s = (SimCard) res[0];
                String con = (String) res[1];
                serviceHall.UpdateConsumption(s,con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stop){
            while(deposit.QueueLength()>0){
                try {
                    Object[] res = deposit.DeQueue();
                    SimCard s = (SimCard) res[0];
                    String de = (String) res[1];
                    serviceHall.UpdateDeposit(s,de);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            while(consumption.QueueLength()>0){
                try {
                    Object[] res = consumption.DeQueue();
                    SimCard s = (SimCard) res[0];
                    String con = (String) res[1];
                    serviceHall.UpdateConsumption(s,con);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            this.cancel();
        }
    }
    public void print_consumption(SimCard s) throws Exception{
        LinkedList<String> results = serviceHall.PrintOutMonthlyConsumption(s);
        if(results == null){
            System.out.println("您好像没有在龙龙移动注册噢，龙龙找不到您的信息");
        }
        else{
            FileWriter fw = new FileWriter("消费记录.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            try{
                System.out.println(s.getTelephone()+"的消费记录:");
                int size = results.size();
                for(int i = 0; i < size; i++){
                    String record = results.remove();
                    System.out.println(record);
                    bw.write(record);
                    fw.flush();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            bw.close();
            fw.close();
        }
        /*if(all_customers.containsValue(s) && consumption.containsKey(s.getTelephone())){
            LinkedList curr = consumption.get(s.getTelephone());

            System.out.println(curr);
            FileWriter fw = new FileWriter("消费记录.txt");
            try{
                System.out.println(s.getTelephone()+"的消费记录:");
                BufferedWriter bw = new BufferedWriter(fw);
                String str = "序号" + '\t' + "用户" + '\t' + "数据" +'\n';
                bw.write(str);
                fw.flush();
                for(int i = 0; i < curr.size(); i++){
                    str = i + '\t'+ s.getTelephone() + '\t' + curr.get(i)+'\n';
                    bw.append(str);
                    bw.flush();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            fw.close();
        }
        else{
            System.out.println("您好像没有在龙龙移动注册噢，龙龙找不到您的信息");
        }*/
    }
    public void print_deposit(SimCard s) throws Exception{
        LinkedList<String> results = serviceHall.PrintOutMonthlyDeposit(s);
        if(results == null){
            System.out.println("您好像没有在龙龙移动注册噢，龙龙找不到您的信息");
        }
        else{
            System.out.println(s.getTelephone()+"的充值记录:");
            FileWriter fw = new FileWriter("充值记录.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                int size = results.size();
                for (int i = 0; i < size; i++) {
                    String record = results.remove();
                    System.out.println(record);
                    bw.write(record);
                    fw.flush();
                }
            } catch (Exception ioe){
                ioe.printStackTrace();
            }
            bw.close();
            fw.close();

        }
        /*if(all_customers.containsValue(s) && deposit.containsKey(s.getTelephone())){
            LinkedList curr = deposit.get(s.getTelephone());
            System.out.println(s.getTelephone()+"的充值记录:");
            System.out.println(curr);
        }
        else{
            System.out.println("您好像没有在龙龙移动注册噢，龙龙找不到您的信息");
        }*/
    }
    public void remove(String phone) throws SQLException {
        all_customers.remove(phone);
        /*consumption.remove(phone);
        deposit.remove(phone);*/
        serviceHall.Delete(phone);
    }
    public boolean getCustomer(String phone) throws Exception {
        return serviceHall.FindCustomer(phone);
    }
    public SimCard getCustomerInfo(String phone, String password) throws Exception {
        return serviceHall.Customer_info(phone,password);
    }
    public SimCard getCustomerToUse(String phone) throws Exception{
        return serviceHall.Customer_use(phone);
    }
    /*public void print_all_customers() throws IOException {
        FileWriter fw = new FileWriter("用户信息.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        for (Map.Entry<String, SimCard> stringSimCardEntry : all_customers.entrySet()) {

            Object key = ((Map.Entry) stringSimCardEntry).getKey();
            SimCard val = (SimCard) ((Map.Entry) stringSimCardEntry).getValue();
            String cus_info = "用户: " + key + ",余额: " + val.getBalance()+",选择套餐: " + val.getMonthly_package().PackageName + '\n';
            bw.write(cus_info);
        }
        bw.close();
        fw.close();
    }*/
}
