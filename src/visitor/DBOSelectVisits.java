package visitor;

import java.util.List;

public interface DBOSelectVisits {
   public List<Visit> getVisitHistoryForVisitor(int visitorId);
}