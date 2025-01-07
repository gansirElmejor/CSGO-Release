public class NetworkConst {
    private final int PORT = 9067;
    private final int TICK = 20;
    private final String FST_SPLIT_REGEX = "&";
    private final String SEC_SPLIT_REGEX = " ";
    private final String LOGIN_REQ = "201";
    private final String NORM_REQ = "200";
    private final String RAY_REQ = "202";
    private final String ATACK_REQ = "203";
    private final String BURN_REQ = "204";
    private final String ERR_REQ = "400";
    private final String INVULNERBLE_REQ = "205";
    private final String INVISIBLE_REQ = "206";
    private final String FROZEN_REQ = "207";
    private final String STUN_REQ = "208";
    private final String NUTRALIZE_REQ = "209";
    private final String KICK_REQ = "210";
    private final String CHANGE_MAP_REQ = "211";
    private final Player BLANK_PLR = new Player("","", null, -1, 0, 0, "Standard");
    private final int MAX_BULLET_PER_BROADCAST = 10000;
    NetworkConst(){}
    public int getPort() {return this.PORT;}
    public int getTick() {return this.TICK;}
    public String getKick() {return this.KICK_REQ;}
    public String getFstRegex() {return this.FST_SPLIT_REGEX;}
    public String getSecRegex() {return this.SEC_SPLIT_REGEX;}
    public String getReqLogin() {return this.LOGIN_REQ;}
    public String getReqNorm() {return this.NORM_REQ;}
    public String getReqAttack() {return this.ATACK_REQ;}
    public String getReqRay() {return this.RAY_REQ;}
    public String getReqErr() {return this.ERR_REQ;}
    public Player getBlankPlayer() {return this.BLANK_PLR;}
    public int getMaxBulletPerBroadcast() {return this.MAX_BULLET_PER_BROADCAST;}
    public String getReqFrozen() {return this.FROZEN_REQ;}
    public String getReqStun() {return this.STUN_REQ;}
    public String getReqNutralize() {return this.NUTRALIZE_REQ;}
    public String getReqBurn() {return this.BURN_REQ;}
    public String getReqInvulnerable() {return this.INVULNERBLE_REQ;}
    public String getReqInvisible() {return this.INVISIBLE_REQ;}
    public String getReqChangeMap() {return this.CHANGE_MAP_REQ;}
}
