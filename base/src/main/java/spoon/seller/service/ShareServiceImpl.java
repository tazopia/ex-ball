package spoon.seller.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import spoon.common.utils.ErrorUtils;
import spoon.mapper.ShareMapper;
import spoon.member.entity.Member;
import spoon.member.service.MemberService;
import spoon.seller.domain.Seller;
import spoon.seller.domain.SellerDto;
import spoon.support.web.AjaxResult;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ShareServiceImpl implements ShareService {

    private ShareMapper shareMapper;

    private MemberService memberService;

    @Override
    public List<Seller> sellerShare(SellerDto.Command command) {
        return shareMapper.sellerShare(command);
    }

    @Transactional
    @Override
    public AjaxResult updateShare(SellerDto.Update update) {
        Member agency2 = memberService.getMember(update.getUserid());

        if ("수익분배".equals(update.getRateCode())) {
            update.setRateSports(0);
            update.setRateZone(0);
        } else {
            update.setRateShare(0);
        }

        try {
            // 정산 방식이 바뀌었으면 하위 대리점도 모두 바뀌어야 한다.
            if (!update.getRateCode().equals(agency2.getRateCode())) {
                updateShareAgency1(update);
            }

            agency2.setRateCode(update.getRateCode());
            agency2.setRateShare(update.getRateShare());
            agency2.setRateSports(update.getRateSports());
            agency2.setRateZone(update.getRateZone());

            memberService.update(agency2);
        } catch (RuntimeException e) {
            log.error("{} - 총판 수수료율 수정에 실패하였습니다.: {}", e.getMessage(), update.toString());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new AjaxResult(false, update.getUserid() + " 총판의 지급율 수정에 실패 하였습니다.");
        }

        return new AjaxResult(true, update.getUserid() + " 총판의 지급율을 수정하였습니다.");
    }

    @Transactional
    @Override
    public AjaxResult updatePartnerShare(SellerDto.Update update) {
        Member agency = memberService.getMember(update.getUserid());

        if ("수익분배".equals(agency.getRateCode())) {
            update.setRateSports(0);
            update.setRateZone(0);
        } else {
            update.setRateShare(0);
        }

        try {
            agency.setRateShare(update.getRateShare());
            agency.setRateSports(update.getRateSports());
            agency.setRateZone(update.getRateZone());

            memberService.update(agency);

        } catch (RuntimeException e) {
            log.error("{} - 대리점 수수료율 수정에 실패하였습니다.: {}", e.getMessage(), update.toString());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new AjaxResult(false, update.getUserid() + " 대리점 지급율 수정에 실패 하였습니다.");
        }

        return new AjaxResult(true, update.getUserid() + " 대리점 지급율을 수정하였습니다.");
    }

    private void updateShareAgency1(SellerDto.Update update) {
        shareMapper.updateRateCode(update);
    }
}
