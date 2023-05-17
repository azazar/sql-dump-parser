/*
 * Copyright (C) 2023 Azazar <spam@azazar.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.azazar.sqldumpparser;

/**
 *
 * @author Mikhail Yevchenko <spam@uo1.net>
 * @since  May 17, 2023
 */
public class SqlInsertParseException extends Exception {

    public SqlInsertParseException(String s, SqlStatement sqlStatement) {
        super(s + " in " + sqlStatement);
    }
    
    public SqlInsertParseException(String s) {
        super(s);
    }

    public WrappedSqlInsertParseException wrap() {
        return new WrappedSqlInsertParseException(this);
    }
    
    static class WrappedSqlInsertParseException extends RuntimeException {

        WrappedSqlInsertParseException(SqlInsertParseException cause) {
            super(cause);
        }

        @Override
        public synchronized SqlInsertParseException getCause() {
            return (SqlInsertParseException)super.getCause();
        }

    }

}
