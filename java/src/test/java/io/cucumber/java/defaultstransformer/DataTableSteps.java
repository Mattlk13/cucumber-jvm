package io.cucumber.java.defaultstransformer;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import org.hamcrest.CoreMatchers;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DataTableSteps {

    private final Author expectedAuthor = new Author("Annie M. G.", "Schmidt", "1911-03-20");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }

    @Given("a list of authors in a table")
    public void aListOfAuthorsInATable(List<Author> authors) {
        assertTrue(authors.contains(expectedAuthor));
    }

    @Given("a single currency in a table")
    public void aSingleCurrencyInATable(Currency currency) {
        assertThat(currency, is(Currency.getInstance("EUR")));
    }

    @Given("a currency in a parameter {}")
    public void aCurrencyInAParameter(Currency currency) {
        assertThat(currency, is(Currency.getInstance("EUR")));
    }


    public static class Author {
        String firstName;
        String lastName;
        String birthDate;

        Author() {
        }

        public Author(String firstName, String lastName, String birthDate) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(String birthDate) {
            this.birthDate = birthDate;
        }

        @Override
        public String toString() {
            return "Author{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Author author = (Author) o;

            if (!firstName.equals(author.firstName)) return false;
            if (!lastName.equals(author.lastName)) return false;
            return birthDate.equals(author.birthDate);
        }

        @Override
        public int hashCode() {
            int result = firstName.hashCode();
            result = 31 * result + lastName.hashCode();
            result = 31 * result + birthDate.hashCode();
            return result;
        }
    }

}
