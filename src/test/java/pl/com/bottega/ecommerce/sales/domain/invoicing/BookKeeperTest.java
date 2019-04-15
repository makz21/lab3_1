package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    BookKeeper bookKeeper;
    InvoiceRequest invoiceRequest1;
    InvoiceRequest invoiceRequest2;
    InvoiceRequest invoiceRequest3;

    @Mock
    ProductData productData;

    @Mock
    TaxPolicy taxPolicy;

    @Before
    public void initialize(){
        bookKeeper = new BookKeeper(new InvoiceFactory());

        MockitoAnnotations.initMocks(this);
        when(productData.getType()).thenReturn(ProductType.STANDARD);

        ClientData clientData = new ClientData(Id.generate(), "client");
        RequestItem item1 = new RequestItem(productData,10, new Money(new BigDecimal(100)));
        RequestItem item2 = new RequestItem(productData,20, new Money(new BigDecimal(200)));

        invoiceRequest1 = new InvoiceRequest(clientData);

        invoiceRequest2 = new InvoiceRequest(clientData);
        invoiceRequest2.add(item1);

        invoiceRequest3 = new InvoiceRequest(clientData);
        invoiceRequest3.add(item1);
        invoiceRequest3.add(item2);

        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(new Money(new BigDecimal(300)),""));

    }

}
