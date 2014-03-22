/** CTest.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package test.java.com.m039.estimoto;

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
        Assert.assertEquals(C.A, 15);
    }            

} // CTest
