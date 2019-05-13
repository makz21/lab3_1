package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;
import pl.com.bottega.ecommerce.system.application.SystemUser;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddProductCommandHandlerTest {

    private AddProductCommandHandler addProductCommandHandler;

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

    private Reservation reservation;
    private Product product;
    private Id clientId;
    private Id orderId;
    private Id productId;

    @Before
    public void initalize(){

        clientId = Id.generate();
        orderId = Id.generate();
        productId = Id.generate();
        command = new AddProductCommand(orderId,productId, 1);
        product = new Product(orderId, Money.ZERO, "Product1", ProductType.STANDARD);
        Client client = new Client();
        ClientData clientData = new ClientData(clientId, "Client");
        reservation = new Reservation(orderId, Reservation.ReservationStatus.OPENED, clientData, new Date());
        when(reservationRepository.load(orderId)).thenReturn(reservation);
        when(productRepository.load(productId)).thenReturn(this.product);
        when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);
        when(systemContext.getSystemUser()).thenReturn(new SystemUser(clientId));
        when(clientRepository.load(clientId)).thenReturn(client);
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
    public void testThatSuggestionServiceShouldNotBeCalledWhenProductIsAvailable(){
        addProductCommandHandler.handle(command);
        verify(suggestionService,times(0)).suggestEquivalent(any(Product.class),any(Client.class));

    }

}
