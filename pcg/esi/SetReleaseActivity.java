package ECN;


import com.lenovo.esi.validate.util.InvocationHandlerHelper;
import com.lenovo.plm.pg.process.common.CommonConstant;
import com.ptc.windchill.esi.tgt.ESITarget;
import com.ptc.windchill.esi.txn.ReleaseActivity;
import com.ptc.windchill.esi.txn.ReleaseStatusType;

import util.PartUtil;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManagerSvr;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.KeywordExpression;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;
import wt.util.WTInvalidParameterException;
import wt.util.WTPropertyVetoException;
import wt.vc.config.LatestConfigSpec;
import wt.vc.views.ViewHelper;

public class SetReleaseActivity {
	protected static PersistenceManagerSvr PM_Service = null;

    public static String releaseclass = "com.ptc.windchill.esi.BOMHeader"; //com.ptc.windchill.esi.BOMHeader,com.ptc.windchill.esi.Part
    public static String searchstatus = "pending";//succeeded,failed
    public static String[] versions = new String[]{"ALL"}; //ALL,LATEST
    public static String[] plants = new String[]{"H001"}; //ALL
    public static String[] partnumbers = new String[]{"20DC0068FE","20DC0053KR","20DC004YCA","20DF004PSP","20DF002FAU","20DF0067CD","20DF003PUS","20DF0047LM","20DF0009ED","20DF000CZA","20DF004ALM","20DF004NFE","20DF0044LM","20DC005XHH","20DC0068PE","20DC0021FE","20DC004VUS","20DC0021UE","20DC0028AU","20DF003RCA","20DF004SIX","20DF003VCA","20DF003MUS","20DF003KCA","20DF003MCA","20DF005YRT","20DH0006HH","20DH0007HH","20DH0005HH","F0BB003LRK","F0BB0006PL","F0BB002SPH","F0BB0018AL","F0BB003QRK","F0BB001NSA","F0BB0008RS","F0BB003BRK","F0BB002YJP","F0BB002JTA","F0BB0027IX","20DC0018TW","20DC001BTW","20DC001NAD","20DC001NFE","20DC001NPE","20DC001RFE","20DC0021PE","20DC0021ZA","20DC0022ED","20DC0022ZA","20DC002AAU","20DC002RAU","20DC002UAU","20DC004BPH","20DC005GJP","20DC0068AD","20DC0068ED","20DC0068UE","20DC0068ZA","20DC006LCD","20DCA00ACD","20DF000FZA","20FXS00903"};
    public static ReleaseStatusType rst = ReleaseStatusType.FAILED; // CHANGE TO
    
    
	public static void main(String[] args) throws WTException, WTPropertyVetoException {
		// TODO Auto-generated method stub
		for(String partnumber : partnumbers){
		    PM_Service = (PersistenceManagerSvr) InvocationHandlerHelper.getInterfaceObject(PersistenceManagerSvr.class);
		        for(String ver : versions){
		            if(ver.equals("ALL")){
		                QueryResult pqr = geallreleasedPartByNumber(partnumber);
		                while(pqr.hasMoreElements()){
		                    WTPart p = (WTPart) pqr.nextElement();
		                    for(String plant : plants){
		                        if(plant.equals("ALL")){
		                            QueryResult raqr = getAllReleaseActivity(p, releaseclass, searchstatus);
		                            while(raqr.hasMoreElements()){
		                                ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                                ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                                ra.setStatus(rst);
		                                PM_Service.update(ra);
		                            }
		                        }else{
		                            ESITarget pla =  getTarget(plant);
		                            QueryResult raqr = getReleaseActivity(p, releaseclass, searchstatus,pla);//failed
		                            while(raqr.hasMoreElements()){
		                                ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                                ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                                ra.setStatus(rst);
		                                PM_Service.update(ra);
		                            }
		                        }
		                        System.out.println(partnumber + "  " + p.getVersionDisplayIdentifier().toString() + " " + plant);
		                    }
		                }
		            }else if(ver.equals("LATEST")){
		                WTPart p = PartUtil.getLatestReleasedPartByNumber(partnumber);
		                for(String plant : plants){
		                if(plant.equals("ALL")){
		                    QueryResult raqr = getAllReleaseActivity(p, releaseclass, searchstatus);
		                    while(raqr.hasMoreElements()){
		                        ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                        ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                        ra.setStatus(rst);
		                        PM_Service.update(ra);
		                    }
		                }else{
		                    ESITarget pla =  getTarget(plant);
		                    QueryResult raqr = getReleaseActivity(p, releaseclass, searchstatus,pla);//failed
		                    while(raqr.hasMoreElements()){
		                        ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                        ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                        ra.setStatus(rst);
		                        PM_Service.update(ra);
		                    }
		                }
		                System.out.println(partnumber + "  " + p.getVersionDisplayIdentifier().toString() + " " + plant);
		                }
		            }else{
		                WTPart p = getWTPartByNumberandVersion(partnumber, ver);
		                for(String plant : plants){
		                if(plant.equals("ALL")){
		                    QueryResult raqr = getAllReleaseActivity(p, releaseclass, searchstatus);
		                    while(raqr.hasMoreElements()){
		                        ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                        ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                        ra.setStatus(rst);
		                        PM_Service.update(ra);
		                    }
		                }else{
		                    ESITarget pla =  getTarget(plant);
		                    QueryResult raqr = getReleaseActivity(p, releaseclass, searchstatus,pla);//failed
		                    while(raqr.hasMoreElements()){
		                        ReleaseActivity ra = (ReleaseActivity) raqr.nextElement();
		                        ra = (ReleaseActivity) PersistenceHelper.manager.refresh(ra);
		                        ra.setStatus(rst);
		                        PM_Service.update(ra);
		                    }
		                }
		                System.out.println(partnumber + "  " + p.getVersionDisplayIdentifier().toString() + " " + plant);
		                }
		            }

		        }
		}
		    

		System.out.println("end");
	}
	
	
	public static QueryResult getReleaseActivity(WTPart part,String releaseClass, String status,ESITarget plant) throws WTException {
		try {
			QuerySpec qsRelAct = new QuerySpec(ReleaseActivity.class);
			SearchCondition scStatus = new SearchCondition(
					ReleaseActivity.class, "status", SearchCondition.EQUAL,
					status);
			SearchCondition scRelClass = new SearchCondition(
					ReleaseActivity.class, "releaseClass",
					SearchCondition.EQUAL, releaseClass);
			SearchCondition scESITarget = new
			SearchCondition(ReleaseActivity.class, "roleBObjectRef.key.id",
			SearchCondition.EQUAL, plant.getPersistInfo().getObjectIdentifier().getId());
			SearchCondition partID = new SearchCondition(ReleaseActivity.class,
					"roleAObjectRef.key.id", SearchCondition.EQUAL, part
							.getPersistInfo().getObjectIdentifier().getId());
			qsRelAct.appendWhere(scStatus);
			qsRelAct.appendAnd();
			qsRelAct.appendWhere(scRelClass);
			qsRelAct.appendAnd();
			qsRelAct.appendWhere(partID);
			qsRelAct.appendAnd();
			qsRelAct.appendWhere(scESITarget);

			// System.out.println("---getReleaseActivity---sql----"+qsRelAct);
			QueryResult qr = PersistenceHelper.manager.find(qsRelAct);
			return qr;
		} catch (WTException e) {
			// TODO Auto-generated catch block
			// System.out.println("---------69----------",e);
			throw e;
		}
	}
	
	public static QueryResult getAllReleaseActivity(WTPart part,String releaseClass, String status) throws WTException {
		try {
			QuerySpec qsRelAct = new QuerySpec(ReleaseActivity.class);
			SearchCondition scStatus = new SearchCondition(
					ReleaseActivity.class, "status", SearchCondition.EQUAL,
					status);
			SearchCondition scRelClass = new SearchCondition(ReleaseActivity.class, "releaseClass",SearchCondition.EQUAL, releaseClass);
			SearchCondition partID = new SearchCondition(ReleaseActivity.class,"roleAObjectRef.key.id", SearchCondition.EQUAL, part.getPersistInfo().getObjectIdentifier().getId());
			qsRelAct.appendWhere(scStatus);
			qsRelAct.appendAnd();
			qsRelAct.appendWhere(scRelClass);
			qsRelAct.appendAnd();
			qsRelAct.appendWhere(partID);

			// System.out.println("---getReleaseActivity---sql----"+qsRelAct);
			QueryResult qr = PersistenceHelper.manager.find(qsRelAct);
			return qr;
		} catch (WTException e) {
			// TODO Auto-generated catch block
			// System.out.println("---------69----------",e);
			throw e;
		}
	}
	
    public static ESITarget getTarget(String name) throws WTException {
        ESITarget target = null;
        try {
            if (target == null) {
                if (name != null) {
                    QuerySpec qs = new QuerySpec(ESITarget.class);
                    qs.appendWhere(new SearchCondition(ESITarget.class, ESITarget.NAME, SearchCondition.EQUAL, name
                            .toUpperCase()));
                    QueryResult qr = PersistenceHelper.manager.find(qs);
                    if (qr.size() == 1) {
                        target = (ESITarget) qr.nextElement();
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return target;
    }
    
	public static WTPart getWTPartByNumberandVersion(String strNumber,
			String strVersion) throws WTException {
		WTPart wtpart = null;
		QueryResult results = null;
		QuerySpec qs = new QuerySpec(WTPart.class);
		int iIndex = qs.getFromClause().getPosition(WTPart.class);
		SearchCondition sc = new SearchCondition(WTPart.class, WTPart.NUMBER,
				SearchCondition.EQUAL, strNumber, false);
		qs.appendWhere(sc, new int[] { iIndex });
		SearchCondition scLatestIteration = new SearchCondition(WTPart.class,
				WTAttributeNameIfc.LATEST_ITERATION, SearchCondition.IS_TRUE);
		if (qs.getConditionCount() > 0)
			qs.appendAnd();
		qs.appendWhere(scLatestIteration, new int[] { iIndex });
		SearchCondition version = new SearchCondition(WTPart.class,
				"versionInfo.identifier.versionId", SearchCondition.EQUAL,
				strVersion);
		if (qs.getConditionCount() > 0)
			qs.appendAnd();
		qs.appendWhere(version, new int[] { iIndex });
		qs.appendAnd();
		SearchCondition scForCheckOutInfo = new SearchCondition(WTPart.class,
				"checkoutInfo.state", SearchCondition.EQUAL, "c/i");
		qs.appendWhere(scForCheckOutInfo, new int[] { iIndex });
		// System.out.println("WTPart SQL = " + qs.toString());
		results = PersistenceHelper.manager.find(qs);

		LatestConfigSpec lsp = new LatestConfigSpec();
		results = lsp.process(results);
		while (results.hasMoreElements()) {
			wtpart = (WTPart) results.nextElement();
		}
		return wtpart;
	}
	
	   public static QueryResult geallreleasedPartByNumber(String partNo) throws WTException {
		   try{
	        WTPart part = null;
	        QuerySpec spec = new QuerySpec(WTPart.class);
	        spec.appendWhere(new SearchCondition(WTPart.class,
	                WTPart.NUMBER, SearchCondition.EQUAL,
	                partNo.toUpperCase()), new int[] { 0 });
	        spec.appendAnd();
	        spec.appendWhere(new SearchCondition(WTPart.class,"view.key.id", SearchCondition.EQUAL,
	                ViewHelper.service.getView(CommonConstant.DESIGN_VIEW).getPersistInfo().getObjectIdentifier()
	                        .getId()), new int[] { 0 });
	        spec.appendAnd();
	        spec.appendWhere(new SearchCondition(new KeywordExpression("A0.statestate"), SearchCondition.EQUAL,
	                new KeywordExpression("'" + CommonConstant.STATE_PRODUCTIONRELEASED + "'")), new int[] { 0 });
	        spec.appendOrderBy(WTPart.class, "thePersistInfo.createStamp", true);
	        QueryResult result = PersistenceHelper.manager.find((StatementSpec) spec);
	        return result;
		   }catch(WTInvalidParameterException we){
			   return null;
		   }catch(IllegalStateException ie){
			   return null;
		   }
	    }
	

}
