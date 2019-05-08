package com.tianjue.referencedatapublisher.instrument;

import com.tianjue.referencedatapublisher.exception.ParseException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;

public class InstrumentDataTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TestName testName = new TestName();

    final String value = "ABC";

    @Test
    public void Given_LMEInstrument_When_str_comform_then_it_parses_correctly() throws ParseException {
        Field[] fields = Field.class.getEnumConstants();
        for(Field field: fields) {
            String str = field + "=" + value + "|";
            if(field != Field.CODE) {
                str += Field.CODE + "=code|";
            }
            InstrumentData instrumentData = new LMEInstrumentData(str);
            When_str_comform_then_it_parses_correctly(instrumentData, field, value);
        }
    }

    @Test
    public void Given_PRIMEInstrument_When_str_comform_then_it_parses_correctly() throws ParseException {
        Field[] fields = Field.class.getEnumConstants();
        for(Field field: fields) {
            String str = field + "=" + value + "|";
            if(field != Field.CODE) {
                str += Field.CODE + "=code|";
            }
            InstrumentData instrumentData = new PRIMEInstrumentData(str);
            When_str_comform_then_it_parses_correctly(instrumentData, field, value);
        }
    }

    @Test
    public void Given_LMEInstrument_does_not_have_code_then_it_throws_exception() throws ParseException {
        expectParseException("Cannot find CODE field");
        Field field = Field.TRADABLE;
        String str = field + "=" + value + "|";
        InstrumentData instrumentData = new LMEInstrumentData(str);
        instrumentData.parse();
    }

    @Test
    public void Given_PRIMEInstrument_does_not_have_code_then_it_throws_exception() throws ParseException {
        expectParseException("Cannot find CODE field");
        Field field = Field.TRADABLE;
        String str = field + "=" + value + "|";
        InstrumentData instrumentData = new PRIMEInstrumentData(str);
        instrumentData.parse();
    }

    @Test
    public void Given_LMEInstrument_When_str_has_pair_issue_then_it_throws_exception() throws ParseException{
        expectParseException("wrong data");
        Field[] fields = Field.class.getEnumConstants();
        for(Field field: fields) {
            String str = field.toString();
            InstrumentData instrumentData = new LMEInstrumentData(str);
            instrumentData.parse();
        }
    }

    @Test
    public void Given_PRIMEInstrument_When_str_has_pair_issue_then_it_throws_exception() throws ParseException{
        expectParseException("wrong data");
        Field[] fields = Field.class.getEnumConstants();
        for(Field field: fields) {
            String str = field.toString();
            InstrumentData instrumentData = new PRIMEInstrumentData(str);
            instrumentData.parse();
        }
    }


    private void When_str_comform_then_it_parses_correctly(InstrumentData instrumentData, Field field, String value) throws ParseException {
        instrumentData.parse();
        Assert.assertEquals(value, instrumentData.getDataMap().get(field));
    }

    private void expectParseException(String message) {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(message);
    }
}
