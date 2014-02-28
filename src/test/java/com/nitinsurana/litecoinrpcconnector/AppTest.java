//package com.nitinsurana.litecoinrpcconnector;
//
//import com.google.gson.JsonObject;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import org.junit.Before;
//import org.junit.BeforeClass;
//
///**
// * Unit test for simple App.
// */
//public class AppTest
//        extends TestCase {
//
//    String txnId = "1a89f40ffc8695867827316fe99dbca1c9cc28bc295085b6a5587b4320acd185";
//
//    /**
//     * Create the test case
//     *
//     * @param testName name of the test case
//     */
//    public AppTest(String testName) {
//        super(testName);
//    }
//
//    /**
//     * @return the suite of tests being tested
//     */
//    public static Test suite() {
//        return new TestSuite(AppTest.class);
//    }
//    App app;
//
//    @Before
//    public void init() throws AuthenticationException {
//        final String rpcUser = "Nitin";
//        final String rpcPassword = "magicmaker07";
//        final String rpcHost = "localhost";
//        final String rpcPort = "9332";
//        app = new App(rpcUser, rpcPassword, rpcHost, rpcPort);
////        app=new App(null, null, null, null)
//    }
//
//    @org.junit.Test
//    public void testBackupWallet() throws Exception {
//        assertTrue(app.backupWallet("c:/nitin.xzy"));
//        assertFalse(app.backupWallet(null));
//        assertFalse(app.backupWallet(""));
//    }
//
//    @org.junit.Test
//    public void testGetRawTransaction() throws Exception {
//        assertNotNull(app.getRawTransaction(txnId));
//    }
//
//    @org.junit.Test
//    public void testDecodeRawTransaction() throws Exception {
////        String txnId = "1a89f40ffc8695867827316fe99dbca1c9cc28bc295085b6a5587b4320acd185";
//        String hex = app.getRawTransaction(txnId);
//        assertTrue(app.decodeRawTransaction(txnId) instanceof JsonObject);
//    }
//
//    @org.junit.Test(expected = RpcInvalidResponseException.class)
//    public void testDumpPrivateKey() throws Exception {
//        String s = app.dumpPrivateKey("LMNtL3ta9Ff69frtecAuZ1LrW63R7fJ2TBD1");
//        assertNotNull(s);
//        //Below should throw Exception
//        app.dumpPrivateKey("LMNtL3ta9Fasdff69frtecAuZ1LrW63R7fJ2TBD1");
//    }
//
//    /**
//     * Rigourous Test :-)
//     */
//    public void testApp() {
//        assertTrue(true);
//    }
//}
