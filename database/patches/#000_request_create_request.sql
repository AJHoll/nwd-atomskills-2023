CREATE OR REPLACE FUNCTION crm_request.request__create_request()
 RETURNS t_id
 LANGUAGE plpgsql
AS $function$
/*<meta object="crm_request.request__insert_record"
        title="Функция создания рандомного заказа"
        author="sys"
        created="26.02.2023"
        version="1.0"
        description="Функция создания рандомного заказа">
</meta>*/
declare
  nv_min_items t_integer := 2;
  nv_max_items t_integer := 12;
  nv_items t_integer := floor(random()*(nv_max_items-nv_min_items) + nv_min_items);

  nv_min_item_qty t_integer := 100;
  nv_max_item_qty t_integer := 1000;
  nv_item_qty t_integer := floor(random()*(nv_max_item_qty-nv_min_item_qty) + nv_min_item_qty);

  idv_random_contractor t_id;

  idv_request t_id;
  idv_request_item t_id;
  dv_release_date t_datetime;
begin
  begin
    select ctr.id
    into idv_random_contractor
    from dictionaries.contractor ctr
    order by ctr.id
    offset floor(random()*(select count(*) 
                           from dictionaries.contractor ctr_in))
    limit 1;
  end;

  idv_request := crm_request.request__insert_record();
  perform crm_request.request__set_contractor(idv_request, idv_random_contractor);
  for i in 1..nv_items loop
    begin
      idv_request_item := crm_request.request_item__insert_record(idv_request
                                                                 ,dictionaries.products__create_product());
      nv_item_qty := floor(random()*(nv_max_item_qty-nv_min_item_qty) + nv_min_item_qty);
      perform crm_request.request_item__set_quantity(idv_request_item, nv_item_qty);
      perform crm_request.request_item__after_edit(idv_request_item);
    exception
      when others then
        null;
    end;
  end loop;
  begin
    select now()::t_date + (interval '1 sec'
          *(coalesce(sum(prod.n_milling_time*req_item.f_quantity),0)
           +coalesce(sum(prod.n_lathe_time*req_item.f_quantity),0)))
    into dv_release_date
    from crm_request.request_item req_item
    inner join dictionaries.products prod on prod.id = req_item.id_product
    where req_item.id_request = idv_request;
  end;
  raise notice '%', dv_release_date;
  perform crm_request.request__set_release_date(idv_request, dv_release_date::t_date);
  perform crm_request.request__after_edit(idv_request);
  return idv_request;
end;
$function$
;

GRANT ALL ON FUNCTION crm_request.request__create_request() TO uni_admin;
GRANT ALL ON FUNCTION crm_request.request__create_request() TO uni_user;