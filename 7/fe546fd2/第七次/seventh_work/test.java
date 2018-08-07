package seventh_work;

import java.awt.*;
import java.util.ArrayList;

import seventh_work.Taxi.status;

import java.util.*;

public class test extends Thread
{
    public long getTime()
    {
        Date d = new Date();
        long t = d.getTime();
        t = t - (t % 100);
        return t;
    }

    public Point taxiPoint(int index,Taxi[] t)
    {
        int x = t[index].get_x();
        int y = t[index].get_y();
        return new Point(x, y);
    }

    public status taxiStatus(int index,Taxi[] t)
    {
        return Taxi.get_state(t[index]);
    }

    public static ArrayList<Integer> taxilist(status st,Taxi[] t)
    {
        ArrayList<Integer> list= new ArrayList<Integer>();
        for (int i=1;i<=100;i++)
            if (Taxi.get_state(t[i]) == st) list.add(i);
        return list;
    }

    @Override
    public void run()
    {
        try{
            //在这里编写测试线程



        }catch (Exception e)
        {
            System.out.println("#Exception");
            System.exit(0);
        }catch (Error e)
        {
            System.out.println("#Error");
            System.exit(0);
        }
    }
}

