import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class calculator {
    /*基本参数定义并初始化*/
    float ans = 0;                  //用于存储答案，初始为0；
    String ans_dis = "0";           //用于存储显示字段，初始为“0”；
    float tmp = 0;                  //用于存储输入，初始为0；
    String act = "+";               //用于存储符号，默认为“+”；
    int arg_point = 0;              //判断是否在小数点之后，默认为0，即不在小数点后；
    static int arg_mtinput1 = 10;   //如果不在小数点后，多次输入处理时使用的常数，处理方式为 tmp = tmp * 10 + new_input，这个参数其实不需要，写出来方便解释，如果必要，可以用来调整进制；
    float arg_mtinput2 = 1;         //如果在小数点后，多次输入处理时使用的变量，arg = arg / 10，tem = tem + new_input * arg;
    
    /*创建UI组件*/
    JFrame calFrame = new JFrame("cal");
    JTextField ansField = new JTextField(ans_dis, 20);  //用于显示答案的文本框，第一个参数是显示的对象，第二个参数是文本框显示的列数（长度）；
    
    JButton button_0 = new JButton("0");    //参数为按钮上显示的内容；
    JButton button_1 = new JButton("1");
    JButton button_2 = new JButton("2");
    JButton button_3 = new JButton("3");
    JButton button_4 = new JButton("4");
    JButton button_5 = new JButton("5");
    JButton button_6 = new JButton("6");
    JButton button_7 = new JButton("7");
    JButton button_8 = new JButton("8");
    JButton button_9 = new JButton("9");
    JButton button_point = new JButton(".");
    JButton button_mtp = new JButton("*");
    JButton button_div = new JButton("/");
    JButton button_add = new JButton("+");
    JButton button_sub = new JButton("-");
    JButton button_eql = new JButton("=");
    JButton button_clear = new JButton("clear");
    
    /*MyCalculator的构造器*/
    public calculator() {
        /*第一步，先把界面做出来*/
        //创建容器，将UI组建放上去
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(4,4,5,5));                     //GridLayout是一个矩阵式布局的控件，用处很大，这里创建一个控件，四个参数分别为行数、列数、横向间隔、纵向间隔；
        pan.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));    //设置四边边距；
        
        pan.add(button_7);
        pan.add(button_8);
        pan.add(button_9);
        pan.add(button_div);
        pan.add(button_4);
        pan.add(button_5);
        pan.add(button_6);
        pan.add(button_mtp);
        pan.add(button_1);
        pan.add(button_2);
        pan.add(button_3);
        pan.add(button_sub);
        pan.add(button_0);
        pan.add(button_point);
        pan.add(button_eql);
        pan.add(button_add);
        
        JPanel pan_up = new JPanel();
        pan_up.setLayout(new BorderLayout());                       //BorderLayout也是一个布局管理器，只有东南西北中五种位置，默认为中间；
        pan_up.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
        ansField.setHorizontalAlignment(JTextField.RIGHT);
        pan_up.add(ansField, BorderLayout.NORTH);
        pan_up.add(button_clear, BorderLayout.SOUTH);
        pan_up.add(button_clear, BorderLayout.EAST);                //这边可以看到，可以重叠使用，即同时靠下并靠右；
        
        //界面设置
        calFrame.setLocation(200, 300);                             //设置窗口在屏幕上的位置；
        calFrame.setResizable(true);                                //设置大小可调整；
        calFrame.getContentPane().setLayout(new BorderLayout());
        calFrame.getContentPane().add(pan_up, BorderLayout.NORTH);
        calFrame.getContentPane().add(pan, BorderLayout.CENTER);
        calFrame.pack();                                            //记得整体打包；
        calFrame.setVisible(true);
        //增加窗口关闭时退出程序的动作，否则不会自动退出
        calFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        /*处理计算逻辑*/
        
        //如果输入的是数字
        class LisnNum implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //取得输入值文本；
                String inputTmp = ((JButton) e.getSource()).getText();
                
                //如果之前没有小数点；
                if(arg_point == 0) {
                    //将输入的文本转换为数字；
                    tmp = tmp * arg_mtinput1 + Integer.parseInt(inputTmp);
                    
                    //调整显示器的显示内容；
                    //因为一开始有设定一个0，就没法直接在后面接上输入的数字，多了一个判断；
                    if(ans_dis.equals("0"))
                        ans_dis = inputTmp;
                    else
                        ans_dis = ans_dis + inputTmp;
                    ansField.setText(ans_dis);
                }
                
                //如果还在小数点后的输入状态；
                else {
                    arg_mtinput2 = arg_mtinput2 / arg_mtinput1;
                    tmp = Integer.parseInt(inputTmp) * arg_mtinput2 + tmp;
                    ans_dis = Float.toString(tmp);
                    ansField.setText(ans_dis);
                }
            }
        }
        
        //如果输入的是小数点
        class LisnPoint implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                arg_point = 1;
                ans_dis = ans_dis + ".";
                ansField.setText(ans_dis);
            }
        }
        
        //如果输入的是符号
        class LisnSign implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //计算上一步的答案；
                if(act.equals("+"))
                    ans = ans + tmp;
                if(act.equals("-"))
                    ans = ans - tmp;
                if(act.equals("*"))
                    ans = ans * tmp;
                if(act.equals("/"))
                    ans = ans / tmp;
                
                //输入暂存复位为0；
                tmp = 0;
                //小数点复位为0，即之后的输入没有小数点；
                arg_point = 0;
                arg_mtinput2 = 1;
                //下一步计算的符号为当前输入的符号；
                act = ((JButton) e.getSource()).getText();  
                
                ans_dis = Float.toString(ans) + act;
                ansField.setText(ans_dis);
                
            }
        }
        
        //如果输入的是等于号
        class LisnEql implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(act.equals("+"))
                    ans = ans + tmp;
                if(act.equals("-"))
                    ans = ans - tmp;
                if(act.equals("*"))
                    ans = ans * tmp;
                if(act.equals("/"))
                    ans = ans / tmp;
                
                ans_dis = Float.toString(ans);
                ansField.setText(ans_dis);
                
                tmp = 0;
                act = "+";
                arg_point = 0;
                arg_mtinput2 = 1;
            }
        }
        
        //如果输入的是clear
        class LisnClear implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //各参数复位；
                ans = 0;
                ans_dis = "0";
                tmp = 0;
                act = "+";
                arg_point = 0;
                arg_mtinput1 = 10;
                arg_mtinput2 = 1;
                ansField.setText(ans_dis);
            }
        }
        
        /*构建运算所需的实体，绑定按钮与运算逻辑*/
        LisnNum lisnNum = new LisnNum();
        LisnPoint lisnPoint = new LisnPoint();
        LisnSign lisnSign = new LisnSign();
        LisnEql lisnEql = new LisnEql();
        LisnClear lisnClear = new LisnClear();
        
        button_0.addActionListener(lisnNum);
        button_1.addActionListener(lisnNum);
        button_2.addActionListener(lisnNum);
        button_3.addActionListener(lisnNum);
        button_4.addActionListener(lisnNum);
        button_5.addActionListener(lisnNum);
        button_6.addActionListener(lisnNum);
        button_7.addActionListener(lisnNum);
        button_8.addActionListener(lisnNum);
        button_9.addActionListener(lisnNum);
        button_point.addActionListener(lisnPoint);
        button_div.addActionListener(lisnSign);
        button_mtp.addActionListener(lisnSign);
        button_add.addActionListener(lisnSign);
        button_sub.addActionListener(lisnSign);
        button_eql.addActionListener(lisnEql);
        button_clear.addActionListener(lisnClear);  
    }
    
    /*主函数*/
    //good
    public static void main(String[] args) {
        /*构建一个计算器实体*/
        calculator calculator = new calculator();
    }
}