package com.swust.kelab.domain;

public class SinaLogin {
    private String retcode;
    private String servertime;
    private String pcid;
    private String nonce;
    private String pubkey;
    private String rsakv;
    private String showpin;
    private String exectime;
    public String getRetcode() {
        return retcode;
    }
    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }
    public String getServertime() {
        return servertime;
    }
    public void setServertime(String servertime) {
        this.servertime = servertime;
    }
    public String getPcid() {
        return pcid;
    }
    public void setPcid(String pcid) {
        this.pcid = pcid;
    }
    public String getNonce() {
        return nonce;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public String getPubkey() {
        return pubkey;
    }
    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }
    public String getRsakv() {
        return rsakv;
    }
    public void setRsakv(String rsakv) {
        this.rsakv = rsakv;
    }
    public String getShowpin() {
        return showpin;
    }
    public void setShowpin(String showpin) {
        this.showpin = showpin;
    }
    public String getExectime() {
        return exectime;
    }
    public void setExectime(String exectime) {
        this.exectime = exectime;
    }

}
