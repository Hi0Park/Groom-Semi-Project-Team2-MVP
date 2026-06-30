package org.example.groommvp.domain.order.service;

import org.example.groommvp.domain.order.dto.PurchaseRequest;
import org.example.groommvp.domain.order.dto.PurchaseResponse;
import org.example.groommvp.domain.order.entity.Order;
import org.example.groommvp.domain.order.entity.OrderItem;
import org.example.groommvp.domain.order.repository.OrderItemRepository;
import org.example.groommvp.domain.order.repository.OrderRepository;
import org.example.groommvp.domain.product.entity.ProductEntity;
import org.example.groommvp.domain.product.repository.ProductRepository;
import org.example.groommvp.domain.stock.entity.Stock;
import org.example.groommvp.domain.stock.entity.StockHistory;
import org.example.groommvp.domain.stock.repository.StockHistoryRepository;
import org.example.groommvp.domain.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {

    private static final String PURCHASE_REASON = "PURCHASE";

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public PurchaseService(
            ProductRepository productRepository,
            StockRepository stockRepository,
            StockHistoryRepository stockHistoryRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public PurchaseResponse purchase(Long productId, PurchaseRequest request) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product was not found.");
        }

        Stock stock = stockRepository.findByProductIdWithPessimisticLock(productId)
                .orElseThrow(() -> new IllegalArgumentException("Stock for product was not found."));
        ProductEntity product = stock.getProduct();
        int quantity = request.quantity();

        stock.decrease(quantity);

        int orderPrice = product.getProduct_price();
        int totalPrice = orderPrice * quantity;
        Order order = orderRepository.save(new Order(totalPrice));
        orderItemRepository.save(new OrderItem(order, product, quantity, orderPrice));
        stockHistoryRepository.save(new StockHistory(stock, order, PURCHASE_REASON, -quantity));

        return new PurchaseResponse(
                order.getId(),
                product.getProduct_id(),
                quantity,
                stock.getQuantity(),
                order.getCreatedAt()
        );
    }
}
