/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.internal.wrapper.Holder;

/**
 * @author nakamura-to
 * 
 */
public class DomainResultProvider<CONTAINER> implements
        ResultProvider<CONTAINER> {

    protected final Supplier<Holder<?, CONTAINER>> supplier;

    protected final BasicFetcher fetcher;

    /**
     * 
     * @param supplier
     * @param query
     */
    public DomainResultProvider(Supplier<Holder<?, CONTAINER>> supplier,
            SelectQuery query) {
        assertNotNull(supplier, query);
        this.supplier = supplier;
        this.fetcher = new BasicFetcher(query);
    }

    @Override
    public CONTAINER get(ResultSet resultSet) throws SQLException {
        Holder<?, CONTAINER> holder = supplier.get();
        fetcher.fetch(resultSet, holder.getWrapper());
        return holder.get();
    }

    @Override
    public CONTAINER getDefault() {
        Holder<?, CONTAINER> holder = supplier.get();
        return holder.getDefault();
    }

}