package com.example.sriram.singleimage2;

/**
 * Created by Sriram on 03-01-2018.
 */

public class list_pre_year_paper
{

    private String subname, semtype,docid, sem, year,qid;
    public list_pre_year_paper(String subname,String sem,String semtype,String year,String qid)
    {
        this.subname=subname;
        this.sem=sem;
        this.semtype=semtype;
        this.year=year;
        this.qid=qid;
    }
    public  void setDocid(String docid)
    {
        this.docid=docid;
    }
    public String getDocid()
    {
        return docid;
    }
    public void setSem(String sem)
    {
        this.sem=sem;
    }
    public String getSem()
    {
        return sem;
    }
    public void setYear(String year)
    {
        this.year=year;
    }
    public String getYear()
    {
        return year;
    }
    public void setSubname(String subname)
    {
        this.subname=subname;
    }
    public String getSubname()
    {
        return subname;
    }
    private void setSemtype(String semtype)
    {
        this.semtype=semtype;
    }
    public String getSemtype()
    {
        return semtype;
    }
    private void setQid(String semtype)
    {
        this.semtype=semtype;
    }
    public String getQid()
    {
        return qid;
    }
}
