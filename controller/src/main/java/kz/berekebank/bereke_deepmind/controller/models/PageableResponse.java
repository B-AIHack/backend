package kz.berekebank.bereke_deepmind.controller.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

  private int  totalPageSize;
  private long totalElementSize;

  private boolean hasNext;

  private List<T> rows;

  public static <T> PageableResponse<T> of(Page<T> page) {
    return PageableResponse.<T>builder()
                           .rows(page.getContent())
                           .totalPageSize(page.getTotalPages())
                           .totalElementSize(page.getTotalElements())
                           .hasNext(page.hasNext())
                           .build();
  }

}
