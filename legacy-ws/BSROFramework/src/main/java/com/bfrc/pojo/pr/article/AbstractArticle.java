// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:57 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AbstractArticle.java

package com.bfrc.pojo.pr.article;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.bfrc.pr.article:
//            Article

public abstract class AbstractArticle
    implements Serializable
{

    public int getArticleFileSize()
    {
        return this.articleFileSize;
    }

    public void setArticleFileSize(int articleFileSize)
    {
        this.articleFileSize = articleFileSize;
    }

    public String getArticleFileType()
    {
        return this.articleFileType;
    }

    public void setArticleFileType(String articleFileType)
    {
        this.articleFileType = articleFileType;
    }

    public byte[] getArticleFile()
    {
        return this.articleFile;
    }

    public void setArticleFile(byte[] articleFile)
    {
        this.articleFile = articleFile;
    }

    public void setArticleFilename(String articleFilename)
    {
        this.articleFilename = articleFilename;
    }

    public String getArticleFilename()
    {
        return this.articleFilename;
    }

    public Date getArticlePublicationDateSearch()
    {
        return this.articlePublicationDateSearch;
    }

    public void setArticlePublicationDateSearch(Date articlePublicationDateSearch)
    {
        this.articlePublicationDateSearch = articlePublicationDateSearch;
    }

    public Long getArticleCreatorId()
    {
        return this.articleCreatorId;
    }

    public void setArticleCreatorId(Long articleCreatorId)
    {
        this.articleCreatorId = articleCreatorId;
    }

    public AbstractArticle()
    {
    	this.hashValue = 0;
    }

    public AbstractArticle(String articleId)
    {
    	this.hashValue = 0;
        setArticleId(articleId);
    }

    public String getArticleAuthor()
    {
        return this.articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor)
    {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleAuthorCredentials()
    {
        return this.articleAuthorCredentials;
    }

    public void setArticleAuthorCredentials(String articleAuthorCredentials)
    {
        this.articleAuthorCredentials = articleAuthorCredentials;
    }

    public String getArticleCopy()
    {
        return this.articleCopy;
    }

    public void setArticleCopy(String articleCopy)
    {
    	this.hashValue = 0;
        this.articleCopy = articleCopy;
    }

    public String getArticleId()
    {
        return this.articleId;
    }

    public void setArticleId(String articleId)
    {
        this.articleId = articleId;
    }

    public String getArticlePublicationDate()
    {
        return this.articlePublicationDate;
    }

    public void setArticlePublicationDate(String articlePublicationDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        if(articlePublicationDate != null && !articlePublicationDate.equals(""))
            try
            {
                Date newDate = sdf.parse(articlePublicationDate);
                setArticlePublicationDateSearch(newDate);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        this.articlePublicationDate = articlePublicationDate;
    }

    public String getArticlePublication()
    {
        return this.articlePublication;
    }

    public void setArticlePublication(String articlePublication)
    {
        this.articlePublication = articlePublication;
    }

    public String getArticleSource()
    {
        return this.articleSource;
    }

    public void setArticleSource(String articleSource)
    {
        this.articleSource = articleSource;
    }

    public String getArticleSubtitle()
    {
        return this.articleSubtitle;
    }

    public void setArticleSubtitle(String articleSubtitle)
    {
        this.articleSubtitle = articleSubtitle;
    }

    public String getArticleTitle()
    {
        return this.articleTitle;
    }

    public void setArticleTitle(String articleTitle)
    {
        this.articleTitle = articleTitle;
    }

    public boolean equals(Object rhs)
    {
        if(rhs == null)
            return false;
        if(!(rhs instanceof Article))
            return false;
        Article that = (Article)rhs;
        return getArticleId() == null || that.getArticleId() == null || getArticleId().equals(that.getArticleId());
    }

    public int hashCode()
    {
        if(this.hashValue == 0)
        {
            int result = 17;
            int articleIdValue = getArticleId() != null ? getArticleId().hashCode() : 0;
            result = result * 37 + articleIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

    private int hashValue;
    private String articleId;
    private String articleTitle;
    private String articleSubtitle;
    private String articleAuthor;
    private String articlePublicationDate;
    private String articleSource;
    private String articleAuthorCredentials;
    private String articlePublication;
    private String articleCopy;
    private Date articlePublicationDateSearch;
    private Long articleCreatorId;
    private String articleFilename;
    private String articleFileType;
    private int articleFileSize;
    private byte[] articleFile;
}