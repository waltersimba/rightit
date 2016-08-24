package co.za.rightit.commons.repository;

import co.za.rightit.commons.utils.Page;
import co.za.rightit.commons.utils.Pageable;

public interface PagingRepository<T> {

	Page<T> findAll(Pageable pageable);
}
