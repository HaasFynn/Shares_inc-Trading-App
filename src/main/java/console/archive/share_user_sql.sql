begin;
use new_shares;

create table share_portfolio
(
    share_id bigint not null,
    user_id  bigint not null,
    amount   int    not null,
    FOREIGN KEY (share_id) references shares (id),
    FOREIGN KEY (user_id) references users (id)
);
commit;