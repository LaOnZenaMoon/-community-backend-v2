package me.lozm.global.documentation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.lozm.code.EnumModel;
import me.lozm.exception.CustomExceptionType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentationUtils {

    public static String PREFIX_DATA = "data.";
    public static String PREFIX_LIST_DATA = "data[].";
    public static String PREFIX_PAGE_DATA = "data.content[].";


    public static List<FieldDescriptor> getSuccessDefaultResponse() {
        return List.of(
                fieldWithPath("code").description("응답 코드값"),
                fieldWithPath("responseDateTime").description("응답일시")
        );
    }

    public static List<FieldDescriptor> getFailDefaultResponse(CustomExceptionType... exceptionTypes) {
        return List.of(
                fieldWithPath("code").description(String.format("응답 코드값%s", makeCodeDescriptions(exceptionTypes))),
                fieldWithPath("message").description("응답 메세지"),
                fieldWithPath("responseDateTime").description("응답일시")
        );
    }

    public static String getAccessToken() {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbklkIjoic3lzdGVtIiwicm9sZSI6IlJPTEVfU1lTVEVNIiwidXNlcl9uYW1lIjoic3lzdGVtIiwic2NvcGUiOlsicmVhZCJdLCJuYW1lIjoi7Iuc7Iqk7YWcIiwiZXhwIjoxNjU2MDM1NjIxLCJ1c2VySWQiOjEsImF1dGhvcml0aWVzIjpbIlJPTEVfU1lTVEVNIl0sImp0aSI6IjBkNDE1ODA0LTc1N2EtNDBlNy1hYjI3LTJmNjkwM2JiMTQyYyIsImNsaWVudF9pZCI6Imxhb256ZW5hbW9vbl9jbGllbnRfaWQifQ.tPE9moqxO9NTn20wrRo0MCY3-6walMYS_eqHESTnOeU";
    }

    public static String getRefreshToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbklkIjoic3lzdGVtIiwicm9sZSI6IlJPTEVfU1lTVEVNIiwidXNlcl9uYW1lIjoic3lzdGVtIiwic2NvcGUiOlsicmVhZCJdLCJhdGkiOiIwZDQxNTgwNC03NTdhLTQwZTctYWIyNy0yZjY5MDNiYjE0MmMiLCJuYW1lIjoi7Iuc7Iqk7YWcIiwiZXhwIjoxNjU2MDM5MjIxLCJ1c2VySWQiOjEsImF1dGhvcml0aWVzIjpbIlJPTEVfU1lTVEVNIl0sImp0aSI6IjEyNzU0MzkxLWJjZWYtNGZjMS05M2E3LWZlYjQwNmIzNWI1YyIsImNsaWVudF9pZCI6Imxhb256ZW5hbW9vbl9jbGllbnRfaWQifQ.PAppsoJp4o_tmHCMQ8ikTz505myZictGFLnF_YoQZBE";
    }

    public static <E extends Enum<E> & EnumModel> String getAllOfEnumModelElementNames(String enumName, Class<E> enumClass) {
        EnumSet<E> enumSet = EnumSet.allOf(enumClass);
        Iterator<E> iterator = enumSet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(enumName);
        while (iterator.hasNext()) {
            E item = iterator.next();
            stringBuilder.append(" + \n - ");
            stringBuilder.append(item.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(item.getValue());
        }
        return stringBuilder.toString();
    }

    public static <E extends Enum<E>> String getAllOfEnumElementNames(String enumName, Class<E> enumClass) {
        EnumSet<E> enumSet = EnumSet.allOf(enumClass);
        Iterator<E> iterator = enumSet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(enumName);
        while (iterator.hasNext()) {
            E item = iterator.next();
            stringBuilder.append(" + \n - ");
            stringBuilder.append(item.name());
        }
        return stringBuilder.toString();
    }


    private static String makeCodeDescriptions(CustomExceptionType... exceptionTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CustomExceptionType exceptionType : exceptionTypes) {
            stringBuilder.append(" + \n - ");
            stringBuilder.append(exceptionType.getCode());
            stringBuilder.append(": ");
            stringBuilder.append(exceptionType.getMessage());
        }
        return stringBuilder.toString();
    }

    public static List<FieldDescriptor> getPageFieldDescriptor() {
        return Arrays.asList(
                fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호 (1부터 시작)"),
                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 개수"),
                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수")
        );
    }


    public static RequestParametersSnippet getPageParameters() {
        return requestParameters(
                parameterWithName("pageNumber").description("페이지 번호 (default = 1)").optional(),
                parameterWithName("pageSize").description("페이지 사이즈 (default = 10)").optional(),
                parameterWithName("pageSort").description("페이지 정렬 + \npageSort=필드명:asc/desc,필드명:asc/desc,... + \nex) pageSort=id:asc,name:desc").optional()
        );

    }
}
