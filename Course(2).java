public class Course
{
	public String code;//global variable
	public int capacity;
	public SLinkedList<Student>[] studentTable;
	public int size;
	public SLinkedList<Student> waitlist;

	public Course(String code)//local variable
	{
		this.code = code;//当前类中的某个成员变量
		this.studentTable = new SLinkedList[10];
		this.size = 0;
		this.waitlist = new SLinkedList<Student>();
		this.capacity = 10;
	}

	public Course(String code, int capacity)
	{
		this.code = code;
		this.studentTable = new SLinkedList[capacity];
		this.size = 0;
		this.waitlist = new SLinkedList<>();
		this.capacity = capacity;
	}

	//把已经坐下的学生挪到新教室
	public void changeArrayLength(int m)
	{
		// insert your solution here
        this.capacity=m;
        SLinkedList<Student>[] studentTable2=new SLinkedList[m];
        //处理原有教室的每一个学生
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                //放到新的位置
                int k=stu.id%m;
                if(studentTable2[k]==null) studentTable2[k]=new SLinkedList<Student>();
                studentTable2[k].addLast(stu);
            }
        }
        studentTable=studentTable2;
	}

	public boolean put(Student s)
	{
		// insert your solution here and modify the return statement
		//是否不合理
        //判断已经坐下的学生是否重复
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                if(stu.id==s.id) return false;
            }
        }
        //等着的学生是否重复
        for(int i=0;i<waitlist.size();i++)
        {
            if(waitlist.get(i).id==s.id) return false;
        }
        //课已经选满了
        if(s.courseCodes.size()>=s.COURSE_CAP) return false;
        //还没有坐满,直接坐下
        if(size<capacity)
        {
            int k = s.id % capacity;
            if (studentTable[k] == null) studentTable[k] = new SLinkedList<>();
            studentTable[k].addLast(s);
            size++;
            return true;
        }
        //等待列表还没有满
        if(waitlist.size()<=capacity/2)
        {
            waitlist.addLast(s);
            return true;
        }
        //等待列表满了，只能换教室
        changeArrayLength((int)(capacity*1.5) );
        //把等着的学生也坐下
        while(waitlist.size()>0)
        {
            Student stu=waitlist.removeFirst();
            int k = stu.id % capacity;
            if (studentTable[k] == null) studentTable[k] = new SLinkedList<>();
            studentTable[k].addLast(stu);
            size++;
        }
        //新来的还是要等着
        waitlist.addLast(s);
        return true;
	}

	public Student get(int id)
	{
		// insert your solution here and modify the return statement
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                if(stu.id==id) return stu;
            }
        }
        for(int i=0;i<waitlist.size();i++)
        {
            Student stu=waitlist.get(i);
            if(stu.id==id) return stu;
        }
        return null;
	}

	public Student remove(int id)
	{
		// insert your solution here and modify the return statement
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                if(stu.id==id)
                {
                    list.remove(j);
                    size--;
                    //等待的列表是否还有学生，有的话拿一个过来
                    if(waitlist.size()>0)
                    {
                        Student s = waitlist.removeFirst();
                        int k = s.id % capacity;
                        if (studentTable[k] == null)
                            studentTable[k] = new SLinkedList<>();
                        studentTable[k].addLast(s);
                        size++;
                    }
                    return stu;
                }
            }
        }
        for(int i=0;i<waitlist.size();i++)
        {
            Student stu=waitlist.get(i);
            if(stu.id==id)
            {
                waitlist.remove(i);
                return stu;
            }
        }
        return null;
	}

	public int getCourseSize()
	{
		// insert your solution here and modify the return statement
		return size;
	}

	public int[] getRegisteredIDs()
	{
		// insert your solution here and modify the return statement
		int ids[]=new int[size];
		int k=0;
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                ids[k]=stu.id;
                k++;
            }
        }
        return ids;
	}

	public Student[] getRegisteredStudents()
	{
		// insert your solution here and modify the return statement
        Student stus[]=new Student[size];
        int k=0;
        for(int i=0;i<studentTable.length;i++)
        {
            SLinkedList<Student> list=studentTable[i];
            if(list==null) continue;
            for(int j=0;j<list.size();j++)
            {
                Student stu=list.get(j);
                stus[k]=stu;
                k++;
            }
        }
        return stus;
	}

	public int[] getWaitlistedIDs()
	{
		// insert your solution here and modify the return statement
		int ids[]=new int[waitlist.size()];
		for(int i=0;i<waitlist.size();i++)
        {
            ids[i]=waitlist.get(i).id;
        }
		return ids;
	}

	public Student[] getWaitlistedStudents()
	{
		// insert your solution here and modify the return statement
        Student stus[]=new Student[waitlist.size()];
        for(int i=0;i<waitlist.size();i++)
        {
            stus[i]=waitlist.get(i);
        }
        return stus;
	}

	public String toString()
	{
		String s = "Course: " + this.code + "\n";
		s += "--------\n";
		for (int i = 0; i < this.studentTable.length; i++)
		{
			s += "|" + i + "     |\n";
			s += "|  ------> ";
			SLinkedList<Student> list = this.studentTable[i];
			if (list != null)
			{
				for (int j = 0; j < list.size(); j++)
				{
					Student student = list.get(j);
					s += student.id + ": " + student.name + " --> ";
				}
			}
			s += "\n--------\n\n";
		}

		return s;
	}
}

