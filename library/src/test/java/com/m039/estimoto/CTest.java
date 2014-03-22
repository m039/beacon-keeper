/** CTest.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.estimoto;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * 
 *
 * Created: 03/22/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
@RunWith(RobolectricTestRunner.class)
public class CTest {
    
    @Test
    public void testC() {
        Assert.assertEquals(C.A, 14);
    }            

} // CTest
