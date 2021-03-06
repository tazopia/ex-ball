package spoon.sale.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import spoon.common.utils.ErrorUtils;
import spoon.common.utils.StringUtils;
import spoon.mapper.ShareMapper;
import spoon.member.domain.Role;
import spoon.member.domain.User;
import spoon.member.service.MemberService;
import spoon.payment.domain.PointCode;
import spoon.payment.service.PaymentService;
import spoon.sale.domain.SaleDto;
import spoon.sale.entity.QSale;
import spoon.sale.entity.QSaleItem;
import spoon.sale.entity.Sale;
import spoon.sale.entity.SaleItem;
import spoon.sale.repository.SaleItemRepository;
import spoon.sale.repository.SaleRepository;
import spoon.support.web.AjaxResult;

import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {

    private MemberService memberService;

    private PaymentService paymentService;

    private ShareMapper shareMapper;

    private SaleRepository saleRepository;

    private SaleItemRepository saleItemRepository;

    private static QSale q = QSale.sale;

    @Override
    public List<SaleItem> currentSale(SaleDto.Command command) {
        return shareMapper.currentSale(command);
    }

    @Transactional
    @Override
    public AjaxResult balanceSale(SaleDto.Command command) {
        User user = memberService.getUser(command.getUserid());
        command.setAgency2(command.getUserid());
        List<SaleItem> saleItems = shareMapper.currentSale(command);
        Sale sale = new Sale();
        sale.setSdate(command.getSdate());
        sale.setEdate(command.getEdate());

        try {
            for (SaleItem item : saleItems) {
                item.setLastMoney(item.getCalcMoney());
                item.setRegDate(command.getEnd());
            }

            sale.setUserid(command.getUserid());
            sale.setAgency1(user.getAgency1());
            sale.setAgency2(user.getAgency2());
            sale.setRole(user.getRole());
            sale.setRegDate(command.getEnd());
            sale.setSaleItems(saleItems);

            saleRepository.saveAndFlush(sale);

        } catch (RuntimeException e) {
            log.error("{} - 총판 정산에 실패하였습니다.: {}", e.getMessage(), command.getUserid());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new AjaxResult(false, command.getUserid() + " 총판 정산에 실패 하였습니다.");
        }

        return new AjaxResult(true, command.getUserid() + " 총판 정산을 완료하였습니다.");
    }

    @Override
    public Page<Sale> getPage(SaleDto.Command command, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.notEmpty(command.getAgency2())) {
            builder.and(q.agency2.eq(command.getAgency2()));
        }
        return saleRepository.findAll(builder, pageable);
    }

    @Override
    public Page<SaleItem> getPageItem(SaleDto.Command command, Pageable pageable) {
        QSaleItem q = QSaleItem.saleItem;
        return saleItemRepository.findAll(q.userid.eq(command.getAgency1()), pageable);
    }

    @Transactional
    @Override
    public AjaxResult payment(SaleDto.Payment payment) {
        Sale sale = saleRepository.findOne(payment.getSaleId());
        if (sale.isClosing()) return new AjaxResult(false, "이미 지급된 정산입니다.");

        try {
            sale.setClosing(true);
            for (SaleItem item : sale.getSaleItems()) {
                if (item.getRole() == Role.AGENCY2) {
                    item.setTotalMoney(payment.getAmount());
                }
                item.setClosing(true);
            }
            paymentService.addPoint(PointCode.SALE, payment.getSaleId(), payment.getUserid(), payment.getAmount(), "");
            saleRepository.saveAndFlush(sale);

        } catch (RuntimeException e) {
            log.error("{} - 총판 정산금 지급에 실패하였습니다.: {}", e.getMessage(), payment.toString());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new AjaxResult(false, payment.getUserid() + " 총판 정산금 지급에 실패 하였습니다.");
        }

        return new AjaxResult(true, "총판 정산금 지급을 완료 하였습니다.");
    }

    @Transactional
    @Override
    public AjaxResult delete(long id) {
        saleRepository.delete(id);
        return new AjaxResult(true, "총판 정산금 내역을 삭제하였습니다.");
    }
}
