package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler addProductCommandHandler;
    @Mock
    private AddProductCommand command;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SuggestionService suggestionService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private SystemContext systemContext;
    @Mock
    private Reservation reservation;
    @Mock
    private Product product;

    @Before
    public void initalize(){
        command = new AddProductCommand(Id.generate(), Id.generate(), 1);
        Client client = new Client();
        when(product.isAvailable()).thenReturn(true);
        when(reservationRepository.load(any())).thenReturn(reservation);
        when(productRepository.load(any())).thenReturn(product);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);
        addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository,
                suggestionService, clientRepository, systemContext);
    }


    @Test
    public void handleShouldReturnOneReservation(){
        addProductCommandHandler.handle(command);
        verify(reservationRepository, times(1)).load(any(Id.class));
    }

    @Test
    public void handleShouldCallLoadProductOnce(){
        addProductCommandHandler.handle(command);
        verify(productRepository,times(1)).load(command.getProductId());
    }

    @Test
    public void handleShouldCallSaveProductOnce(){
        addProductCommandHandler.handle(command);
        verify(reservationRepository,times(1)).save(any(Reservation.class));
    }

    @Test
    public void testThatProductIsAvailable(){
        Assert.assertThat(product.isAvailable(),is(true));
    }
}
