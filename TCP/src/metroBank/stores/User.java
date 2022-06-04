package metroBank.stores;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class User implements Serializable {
    private static final DateFormat dateFormatter=new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat dateParser = dateFormatter;

    private String name;
    private String surName;
    private String sex;
    private Date birthday;

    public User(){
        name="Невідомо";
        surName="Невідомо";
        sex="Невідомо";
        try{
            birthday = dateParser.parse("01.01.2001");
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public User(String nm, String srn, String sx, String bday){
        super();
        if(nm!=null&&!nm.isEmpty())name=nm;
        else name="Невідомо";
        if(srn!=null&&!srn.isEmpty())surName=srn;
        else surName="Невідомо";
        if(sx!=null&&!sx.isEmpty())sex=sx;
        else sex="Невідомо";
        try{
            birthday=dateParser.parse(bday);
        }catch (ParseException e){
            birthday = new Date(1, Calendar.FEBRUARY, 2001);
            e.printStackTrace();
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getStrBirthday(){
        return dateFormatter.format(birthday);
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getSurName() {
        return surName;
    }

    public void setBirthdayDate(Date birthday) {
        if(birthday!=null)
            this.birthday = birthday;
    }

    public void setBirthday(String date){
        if(date!=null&&!date.isEmpty())
            try{
                birthday=new SimpleDateFormat().parse(date);
            }catch (ParseException e){
                e.printStackTrace();
            }
    }

    public void setName(String name) {
        if(name!=null&&!name.isEmpty())
            this.name = name;
    }

    public void setSex(String sex) {
        if(sex!=null&&!sex.isEmpty())
            this.sex = sex;
    }

    public void setSurName(String surName) {
        if(surName!=null&&!surName.isEmpty())
            this.surName = surName;
    }

    @Override
    public String toString() {
        return name + " " + surName
                +" " + sex +" "+ dateFormatter.format(birthday);
    }
}

