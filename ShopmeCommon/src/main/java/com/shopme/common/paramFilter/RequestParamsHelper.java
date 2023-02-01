package com.shopme.common.paramFilter;

import com.shopme.common.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;

public class RequestParamsHelper {
    private static final String DEFAULT_FIELD_SORT = "name";
    public static final Integer ITEMS_PER_PAGE = 10;

    public static Pageable getPageableFromParamRequest(HashMap<String, String> requestParams) {
        Sort sort = Sort.by(DEFAULT_FIELD_SORT).ascending();
        int pageIndex = 0;

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            PageParamFilter enumKey;
            try {
                enumKey = PageParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case order: {
                    if (value.equals("desc")) sort = sort.descending();
                    else sort = sort.ascending();
                    break;
                }

                case sortBy: {
                    sort = sort.by(value);
                    break;
                }

                case page: {
                    if (StringUtils.isInteger(value))
                        pageIndex = Integer.valueOf(value) - 1;
                    break;
                }
            }
        }

        return PageRequest.of(pageIndex, ITEMS_PER_PAGE, sort);
    }
}
