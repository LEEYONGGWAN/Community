package io.github.seccoding.web.pager.explorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.decorator.Decorator;

public class ClassicPageExplorer extends PageExplorer {

	private Logger logger = LoggerFactory.getLogger("io.github");
	
	public ClassicPageExplorer(Pager pager) {
		this.pager = pager;
		this.decorator = new Decorator();
	}
	
	protected String generate(StringBuffer pagenation) {

        if (pager.nowGroupNumber > 0) {
        	pagenation.append(makePrevGroup(pager.prevGroupPageNumber));
        }

        int nextPrintPage = pager.groupStartPage + pager.printPage;
        if (nextPrintPage > pager.totalPage) {
            nextPrintPage = pager.totalPage + 1;
        }

        logger.info("pager.groupStartPage = " + pager.groupStartPage);
        logger.info("nextPrintPage = " + nextPrintPage);
        
        String pageNumber = "";

        for (int i = pager.groupStartPage; i < nextPrintPage; i++) {
        		
        	pageNumber = decorator.makePageNumber(pageFormat, i);
            
        	logger.info("(i-1) = " + (i-1));
        	
            if ((i-1) == pager.pageNo) {
            	pageNumber = makeHighlightNowPageNumber(pageNumber);
            }
            
            pagenation.append(makePageNumbers(i-1, pageNumber));
        }

        if (pager.nowGroupNumber < (pager.totalGroup - 1)) {
        	pagenation.append(makeNextGroup(pager.nextGroupPageNumber));
        }

        return pagenation.toString();
    }
	
}
