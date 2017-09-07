package by.bsu.machulski.tag;

import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.type.UserRole;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AdminTag extends TagSupport {
    @Override
    public int doStartTag() throws JspException {
        if (UserRole.ADMINISTRATOR.equals(pageContext.getSession().getAttribute(SessionAttributeConstant.ROLE))) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }
}
