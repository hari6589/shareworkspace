// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2/6/2006 9:13:27 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AddressNotFoundException.java

package com.bfrc.storelocator.util;


// Referenced classes of package com.bfrc.storelocator.util:
//            LocatorException

public class FirstlogicDownException extends LocatorException
{

    public FirstlogicDownException()
    {
        super("The Firstlogic server could not be contacted");
    }
}