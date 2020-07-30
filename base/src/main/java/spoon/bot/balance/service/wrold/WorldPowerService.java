package spoon.bot.balance.service.wrold;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spoon.bet.entity.BetItem;
import spoon.bet.service.BetGameService;
import spoon.bot.balance.domain.WorldResult;
import spoon.bot.balance.entity.PolygonBalance;
import spoon.bot.balance.repository.PolygonBalanceRepository;
import spoon.common.net.HttpParsing;
import spoon.common.utils.DateUtils;
import spoon.common.utils.JsonUtils;
import spoon.config.domain.Config;
import spoon.game.domain.MenuCode;
import spoon.gameZone.power.Power;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorldPowerService {

    private final BetGameService betGameService;

    private final PolygonBalanceRepository polygonBalanceRepository;

    @Async
    public void balance() {

        double rate = Config.getSiteConfig().getBalance().getRate();
        long min = Config.getSiteConfig().getBalance().getMin();
        if (min <= 0) min = 1;

        if (rate == 0) return;
        StringBuilder sb = new StringBuilder();

        // 파워볼을 가져오자
        String json = HttpParsing.getJson(Config.getSysConfig().getZone().getPowerUrl());
        if (json == null) {
            return;
        }
        Power power = JsonUtils.toModel(json, Power.class);
        if (power == null) {
            return;
        }
        String sdate = LocalDateTime.parse(power.getSdate(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                .plusMinutes(5).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String round = String.valueOf(power.getRound() + 1);
        int times = (power.getTimes() + 1) % 288;

        log.error("{} - {} - {}", round, sdate, times);


        long calc;
        long pOdd = 0, pEven = 0, odd = 0, even = 0, over = 0, under = 0, pOver = 0, pUnder = 0;
        long size1 = 0, size2 = 0, size3 = 0;
        long odd_over = 0, odd_under = 0, even_over = 0, even_under = 0;
        long pOdd_over = 0, pOdd_under = 0, pEven_over = 0, pEven_under = 0;
        long odd_size1 = 0, odd_size2 = 0, odd_size3 = 0, even_size1 = 0, even_size2 = 0, even_size3 = 0;

        long amount = 0;

        Iterable<BetItem> items = betGameService.getBalanceBet(MenuCode.POWER, sdate);

        for (BetItem item : items) {
            switch (item.getSpecial()) {
                case "pb_oddeven": // 파워볼 홀짝
                    if ("home".equals(item.getBetTeam())) {
                        pOdd += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        pEven += item.getBetMoney();
                    }
                    break;
                case "oddeven": // 일반볼 홀짝
                    if ("home".equals(item.getBetTeam())) {
                        odd += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        even += item.getBetMoney();
                    }
                    break;
                case "pb_overunder": // 파워볼 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        pOver += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        pUnder += item.getBetMoney();
                    }
                    break;
                case "overunder": // 일반볼 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        over += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        under += item.getBetMoney();
                    }
                    break;
                case "size": // 대중소
                    if ("home".equals(item.getBetTeam())) {
                        size3 += item.getBetMoney();
                    } else if ("draw".equals(item.getBetTeam())) {
                        size2 += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        size1 += item.getBetMoney();
                    }
                    break;
                case "odd_ou": // 홀 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        odd_over += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        odd_under += item.getBetMoney();
                    }
                    break;
                case "even_ou": // 짝 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        even_over += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        even_under += item.getBetMoney();
                    }
                    break;
                case "pb_odd_ou": // 파워볼 홀 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        pOdd_over += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        pOdd_under += item.getBetMoney();
                    }
                    break;
                case "pb_even_ou": // 짝 오버언더
                    if ("home".equals(item.getBetTeam())) {
                        pEven_over += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        pEven_under += item.getBetMoney();
                    }
                    break;
                case "odd_size": // 홀 대중소
                    if ("home".equals(item.getBetTeam())) {
                        odd_size3 += item.getBetMoney();
                    } else if ("draw".equals(item.getBetTeam())) {
                        odd_size2 += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        odd_size1 += item.getBetMoney();
                    }
                    break;
                case "even_size": // 짝 대중소
                    if ("home".equals(item.getBetTeam())) {
                        even_size3 += item.getBetMoney();
                    } else if ("draw".equals(item.getBetTeam())) {
                        even_size2 += item.getBetMoney();
                    } else if ("away".equals(item.getBetTeam())) {
                        even_size1 += item.getBetMoney();
                    }
                    break;
            }
        }

        // 파워볼 홀짝
        calc = (long) ((pOdd - pEven) * rate / 100);
        if (calc >= min) {
            sb.append("&po=").append(calc);
            amount += calc;
        }
        calc = (long) ((pEven - pOdd) * rate / 100);
        if (calc >= min) {
            sb.append("&pe=").append(calc);
            amount += calc;
        }

        // 파워볼 오버언더
        calc = (long) ((pOver - pUnder) * rate / 100);
        if (calc >= min) {
            sb.append("&pov=").append(calc);
            amount += calc;
        }
        calc = (long) ((pUnder - pOver) * rate / 100);
        if (calc >= min) {
            sb.append("&pun=").append(calc);
            amount += calc;
        }

        // 일반볼 홀짝
        calc = (long) ((odd - even) * rate / 100);
        if (calc >= min) {
            sb.append("&o=").append(calc);
            amount += calc;
        }
        calc = (long) ((even - odd) * rate / 100);
        if (calc >= min) {
            sb.append("&e=").append(calc);
            amount += calc;
        }

        // 일반볼 오버언더
        calc = (long) ((over - under) * rate / 100);
        if (calc >= min) {
            sb.append("&ov=").append(calc);
            amount += calc;
        }
        calc = (long) ((under - over) * rate / 100);
        if (calc >= min) {
            sb.append("&un=").append(calc);
            amount += calc;
        }

        // 대중소
        calc = (long) (size1 * rate / 100);
        if (calc >= min) {
            sb.append("&s=").append(calc);
            amount += calc;
        }
        calc = (long) (size2 * rate / 100);
        if (calc >= min) {
            sb.append("&m=").append(calc);
            amount += calc;
        }
        calc = (long) (size3 * rate / 100);
        if (calc >= min) {
            sb.append("&l=").append(calc);
            amount += calc;
        }

        // 일반 홀짝 오버언더
        calc = (long) (odd_over * rate / 100);
        if (calc >= min) {
            sb.append("&o+ov=").append(calc);
            amount += calc;
        }
        calc = (long) (odd_under * rate / 100);
        if (calc >= min) {
            sb.append("&o+un=").append(calc);
            amount += calc;
        }
        calc = (long) (even_over * rate / 100);
        if (calc >= min) {
            sb.append("&e+ov=").append(calc);
            amount += calc;
        }
        calc = (long) (even_under * rate / 100);
        if (calc >= min) {
            sb.append("&e+un=").append(calc);
            amount += calc;
        }

        // 파워볼 홀짝 오버언더
        calc = (long) (pOdd_over * rate / 100);
        if (calc >= min) {
            sb.append("&po+pov=").append(calc);
            amount += calc;
        }
        calc = (long) (pOdd_under * rate / 100);
        if (calc >= min) {
            sb.append("&po+pun=").append(calc);
            amount += calc;
        }
        calc = (long) (pEven_over * rate / 100);
        if (calc >= min) {
            sb.append("&pe+pov=").append(calc);
            amount += calc;
        }
        calc = (long) (pEven_under * rate / 100);
        if (calc >= min) {
            sb.append("&pe+pun=").append(calc);
            amount += calc;
        }

        // 일반볼 홀짝 대중소
        calc = (long) (odd_size1 * rate / 100);
        if (calc >= min) {
            sb.append("&o+s=").append(calc);
            amount += calc;
        }
        calc = (long) (odd_size2 * rate / 100);
        if (calc >= min) {
            sb.append("&o+m=").append(calc);
            amount += calc;
        }
        calc = (long) (odd_size3 * rate / 100);
        if (calc >= min) {
            sb.append("&o+l=").append(calc);
            amount += calc;
        }
        calc = (long) (even_size1 * rate / 100);
        if (calc >= min) {
            sb.append("&e+s=").append(calc);
            amount += calc;
        }
        calc = (long) (even_size2 * rate / 100);
        if (calc >= min) {
            sb.append("&e+m=").append(calc);
            amount += calc;
        }
        calc = (long) (even_size3 * rate / 100);
        if (calc >= min) {
            sb.append("&e+l=").append(calc);
            amount += calc;
        }


        if (sb.length() == 0) return;


        String url = Config.getSiteConfig().getBalance().getHost()
                + "?api_token=" + Config.getSiteConfig().getBalance().getKey()
                + "&round=" + round
                + sb.toString();
        sendQuery(url, sdate, round, String.valueOf(times), sb.toString(), amount);

        log.error("-------------------------------------------------------------------------");
        log.error("{}", url);
    }

    private void sendQuery(String param, String betDate, String round, String times, String betType, long price) {
        String json = HttpParsing.getJson(param);
        if (json == null) return;
        WorldResult result = JsonUtils.toModel(json, WorldResult.class);
        if (result == null) return;
        PolygonBalance b = new PolygonBalance();
        b.setGame("파워볼");
        b.setGameDate(betDate);
        b.setRound(round);
        b.setGameType(times + " 회차");
        b.setBetType(betType);
        b.setPrice(price);
        b.setRegDate(new Date());
        b.setMessage(result.isResult() ? "보험성공 : point " + result.getPoint() : result.getMessage());
        polygonBalanceRepository.saveAndFlush(b);
    }

}
